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

    internal class SpringAutoServiceProcessor(environment: SymbolProcessorEnvironment) :
        AbstractProcessor(environment) {

        override fun supportAnnotation(): String = "com.livk.auto.service.annotation.SpringAutoService"

        override fun accept(annotation: KSAnnotation, symbolAnnotation: KSClassDeclaration) {
            val implService = getArgument(annotation, "value") as KSType
            var providerName = implService.declaration.closestClassDeclarationBinaryName()
            if (providerName == Annotation::class.qualifiedName) {
                providerName = "org.springframework.boot.autoconfigure.AutoConfiguration"
            }
            providers.put(providerName, symbolAnnotation.toBinaryName() to symbolAnnotation.containingFile!!)
        }

        override fun generateAndClearConfigFiles() {
            if (!providers.isEmpty) {
                for (annotationName in providers.keySet()) {
                    val resourceFile = "META-INF/spring/${annotationName}.imports"
                    logger.info(supportAnnotation() + " on resource file: $resourceFile")
                    try {
                        val autoConfigurationImpls = providers[annotationName].map { it.first }
                        logger.info(supportAnnotation() + " file contents: $autoConfigurationImpls")
                        val ksFiles = providers[annotationName].map { it.second }.toTypedArray()
                        val dependencies = Dependencies(true, *ksFiles)
                        generator.createNewFile(dependencies, "", resourceFile, "").bufferedWriter().use { writer ->
                            for (configuration in autoConfigurationImpls) {
                                writer.write(configuration)
                                writer.newLine()
                            }
                        }
                        logger.info(supportAnnotation() + " wrote to: $resourceFile")
                    } catch (e: IOException) {
                        logger.error(supportAnnotation() + " unable to create $resourceFile, $e")
                    }
                }
                providers.clear()
            }
        }
    }
}
