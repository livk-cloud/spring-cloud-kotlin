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
            if (!providers.isEmpty) {
                for (providerInterface in providers.keySet()) {
                    val resourceFile = "META-INF/services/${providerInterface}"
                    logger.info(supportAnnotation() + " working on resource file: $resourceFile")
                    try {
                        val autoService = providers[providerInterface].map { it.first }
                        logger.info(supportAnnotation() + " file contents: $autoService")
                        val dependencies =
                            Dependencies(true, *providers[providerInterface].map { it.second }.toTypedArray())
                        generator.createNewFile(dependencies, "", resourceFile, "").bufferedWriter().use { writer ->
                            for (service in autoService) {
                                writer.write(service)
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
