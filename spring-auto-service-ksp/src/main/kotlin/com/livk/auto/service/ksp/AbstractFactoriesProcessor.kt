package com.livk.auto.service.ksp

import com.google.common.collect.Sets
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import java.io.IOException

/**
 * <p>
 * AbstractFactoriesProcessorProvider
 * </p>
 *
 * @author livk
 * @date 2025/4/26
 */
internal abstract class AbstractFactoriesProcessor(environment: SymbolProcessorEnvironment) : AbstractProcessor(environment) {

    protected abstract fun getLocation(): String

    override fun accept(annotation: KSAnnotation, symbolAnnotation: KSClassDeclaration) {
        val implService = getArgumentValue(annotation) as KSType
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
        providers.put(
            providerName,
            symbolAnnotation.toBinaryName() to symbolAnnotation.containingFile!!
        )
    }

    override fun generateAndClearConfigFiles() {
        val resourceFile = getLocation()
        val implServiceSeq = providers
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
