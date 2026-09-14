package com.livk.auto.service.ksp

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import java.io.IOException

/**
 * @author livk
 */
@AutoService(SymbolProcessorProvider::class)
class SpringAutoServiceProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        SpringAutoServiceProcessor(environment)

    private class SpringAutoServiceProcessor(environment: SymbolProcessorEnvironment) :
        AbstractProcessor(environment) {

        override fun supportAnnotation(): String = "com.livk.auto.service.annotation.SpringAutoService"

        override fun accept(annotation: KSAnnotation, symbolAnnotation: KSClassDeclaration) {
            val implService = getArgumentValue(annotation) as KSType
            var providerName = implService.declaration.closestClassDeclarationBinaryName()
            if (providerName == Annotation::class.qualifiedName) {
                providerName = "org.springframework.boot.autoconfigure.AutoConfiguration"
            }
            providers.put(providerName, symbolAnnotation.toBinaryName() to symbolAnnotation.containingFile!!)
        }

        override fun generateAndClearConfigFiles() {
            if (providers.isEmpty) {
                return
            }
            try {
                for (annotationName in providers.keySet()) {
                    val resourceFile = "META-INF/spring/$annotationName.imports"
                    logger.info("${supportAnnotation()} working on resource file: $resourceFile")
                    try {
                        // .imports 文件按字典序排序，保证产物在多次编译间内容稳定可复现
                        val autoConfigurationImpls = providers[annotationName].map { it.first }.toSortedSet()
                        logger.info("${supportAnnotation()} file contents: $autoConfigurationImpls")
                        val ksFiles = providers[annotationName].map { it.second }.toTypedArray()
                        val dependencies = Dependencies(true, *ksFiles)
                        generator.createNewFile(dependencies, "", resourceFile, "").bufferedWriter().use { writer ->
                            for (configuration in autoConfigurationImpls) {
                                writer.write(configuration)
                                writer.newLine()
                            }
                        }
                        logger.info("${supportAnnotation()} wrote to: $resourceFile")
                    } catch (e: IOException) {
                        logger.error("${supportAnnotation()} unable to create $resourceFile, $e")
                    }
                }
            } finally {
                // 无论生成成功还是失败都清空，避免残留状态被带入 KSP 的下一处理轮次
                providers.clear()
            }
        }
    }
}
