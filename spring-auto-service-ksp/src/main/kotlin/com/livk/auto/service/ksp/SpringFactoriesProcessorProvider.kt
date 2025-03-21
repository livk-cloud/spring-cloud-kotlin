package com.livk.auto.service.ksp

import com.google.auto.service.AutoService
import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Multimaps
import com.google.common.collect.Sets
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType
import java.io.IOException

/**
 * @author livk
 */
@AutoService(SymbolProcessorProvider::class)
class SpringFactoriesProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        SpringFactoriesProcessor(environment)

    internal class SpringFactoriesProcessor(environment: SymbolProcessorEnvironment) : AbstractProcessor(environment) {

        companion object {
            const val SPRING_LOCATION: String = "META-INF/spring.factories"

            const val AOT_LOCATION: String = "META-INF/spring/aot.factories"
        }

        private val aotFactoriesMap =
            Multimaps.synchronizedSetMultimap(LinkedHashMultimap.create<String, Pair<String, KSFile>>())

        override fun supportAnnotation(): String = "com.livk.auto.service.annotation.SpringFactories"

        override fun accept(annotation: KSAnnotation, symbolAnnotation: KSClassDeclaration) {
            val implService = getArgument(annotation, "value") as KSType
            var providerName = implService.declaration.closestClassDeclarationBinaryName()
            if (providerName == Void::class.java.name) {
                val interfaceList = symbolAnnotation.superTypes
                    .map { it.resolve().declaration }
                    .filter { resolver.getClassDeclarationByName(it.simpleName)?.classKind == ClassKind.INTERFACE }
                providerName = if (interfaceList.count() == 1) {
                    interfaceList.first().closestClassDeclarationBinaryName()
                } else {
                    ""
                }
            }
            val aotSupport = getArgument(annotation, "aot") as Boolean
            if (aotSupport) {
                aotFactoriesMap.put(
                    providerName,
                    symbolAnnotation.toBinaryName() to symbolAnnotation.containingFile!!
                )
            } else {
                providers.put(
                    providerName,
                    symbolAnnotation.toBinaryName() to symbolAnnotation.containingFile!!
                )
            }
        }

        override fun generateAndClearConfigFiles() {
            for (pair in listOf(SPRING_LOCATION to providers, AOT_LOCATION to aotFactoriesMap)) {
                val resourceFile = pair.first
                val implServiceSeq = pair.second
                if (!implServiceSeq.isEmpty) {
                    val dependencies = Dependencies(true, *implServiceSeq.values().map { it.second }.toTypedArray())
                    generator.createNewFile(dependencies, "", resourceFile, "").bufferedWriter().use { writer ->
                        for (providerInterface in implServiceSeq.keySet()) {
                            logger.info("Working on resource file: $resourceFile")
                            try {
                                val allServices =
                                    Sets.newTreeSet(HashSet(implServiceSeq[providerInterface].map { it.first }))
                                logger.info("New service file contents: $allServices")
                                writer.write("${providerInterface}=\\")
                                writer.newLine()
                                for ((index, service) in allServices.withIndex()) {
                                    writer.write(service)
                                    if (index != allServices.count() - 1) {
                                        writer.write(",\\")
                                    }
                                    writer.newLine()
                                }
                                writer.newLine()
                                logger.info("Wrote to: $resourceFile")
                            } catch (e: IOException) {
                                logger.error("Unable to create $resourceFile, $e")
                            }
                        }
                        implServiceSeq.clear()
                    }
                }
            }
        }

    }
}
