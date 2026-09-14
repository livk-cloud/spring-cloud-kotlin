package com.livk.auto.service.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import java.io.IOException

/**
 * <p>
 * GoogleAutoServiceProcessorProvider
 * </p>
 *
 * @author livk
 * @date 2024/6/20
 */
class GoogleAutoServiceProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        GoogleAutoServiceProcessor(environment)

    private class GoogleAutoServiceProcessor(environment: SymbolProcessorEnvironment) :
        AbstractProcessor(environment) {

        override fun supportAnnotation(): String = "com.google.auto.service.AutoService"

        override fun accept(annotation: KSAnnotation, symbolAnnotation: KSClassDeclaration) {
            for (any in getArgumentValue(annotation) as List<*>) {
                val implService = any as KSType
                val providerName = implService.declaration.closestClassDeclarationBinaryName()
                providers.put(providerName, symbolAnnotation.toBinaryName() to symbolAnnotation.containingFile!!)
            }
        }

        override fun generateAndClearConfigFiles() {
            if (providers.isEmpty) {
                return
            }
            try {
                for (providerInterface in providers.keySet()) {
                    val resourceFile = "META-INF/services/$providerInterface"
                    logger.info("${supportAnnotation()} working on resource file: $resourceFile")
                    try {
                        // ServiceLoader 规范下条目顺序无关紧要，排序只是为了保证产物字节可复现
                        val autoService = providers[providerInterface].map { it.first }.toSortedSet()
                        logger.info("${supportAnnotation()} file contents: $autoService")
                        val dependencies =
                            Dependencies(true, *providers[providerInterface].map { it.second }.toTypedArray())
                        generator.createNewFile(dependencies, "", resourceFile, "").bufferedWriter().use { writer ->
                            for (service in autoService) {
                                writer.write(service)
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
