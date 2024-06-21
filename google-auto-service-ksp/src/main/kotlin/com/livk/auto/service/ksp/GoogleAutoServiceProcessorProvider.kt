package com.livk.auto.service.ksp

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType
import java.io.IOException
import java.util.SortedSet

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

    internal class GoogleAutoServiceProcessor(environment: SymbolProcessorEnvironment) :
        AbstractProcessor(environment) {

        override fun supportAnnotation(): String = "com.google.auto.service.AutoService"

        override fun accept(annotation: KSAnnotation, symbolAnnotation: KSClassDeclaration) {
            for (any in getArgument(annotation, "value") as List<*>) {
                val implService = any as KSType
                val providerName = implService.declaration.closestClassDeclaration()?.toBinaryName()
                providers.put(providerName, symbolAnnotation.toBinaryName() to symbolAnnotation.containingFile!!)
            }
        }

        override fun generateAndClearConfigFiles() {
            if (!providers.isEmpty) {
                for (providerInterface in providers.keySet()) {
                    val resourceFile = "META-INF/service/${providerInterface}"
                    logger.info("Working on resource file: $resourceFile")
                    try {
                        val allServices: SortedSet<String> = Sets.newTreeSet()
                        val foundImplementers = providers[providerInterface]
                        val newServices: Set<String> = HashSet(foundImplementers.map { it.first })
                        allServices.addAll(newServices)
                        logger.info("New service file contents: $allServices")
                        val ksFiles = foundImplementers.map { it.second }
                        logger.info("Originating files: ${ksFiles.map(KSFile::fileName)}")
                        val dependencies = Dependencies(true, *ksFiles.toTypedArray())
                        generator.createNewFile(dependencies, "", resourceFile, "").bufferedWriter().use { writer ->
                            for (service in allServices) {
                                writer.write(service)
                                writer.newLine()
                            }
                        }
                        logger.info("Wrote to: $resourceFile")
                    } catch (e: IOException) {
                        logger.error("Unable to create $resourceFile, $e")
                    }
                }
                providers.clear()
            }
        }
    }
}
