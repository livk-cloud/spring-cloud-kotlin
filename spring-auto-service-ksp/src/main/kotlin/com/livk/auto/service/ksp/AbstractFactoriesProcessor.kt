package com.livk.auto.service.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import java.io.IOException

/**
 * <p>
 * AbstractFactoriesProcessor
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
                .filterIsInstance<KSClassDeclaration>()
                .filter { it.classKind == ClassKind.INTERFACE }
            if (interfaceList.count() != 1) {
                logger.error(
                    "${symbolAnnotation.toBinaryName()} unable to determine a unique interface " +
                        "(found ${interfaceList.count()}), please specify the target interface explicitly via value",
                    symbolAnnotation
                )
                return
            }
            providerName = interfaceList.first().closestClassDeclarationBinaryName()
        }
        providers.put(
            providerName,
            symbolAnnotation.toBinaryName() to symbolAnnotation.containingFile!!
        )
    }

    override fun generateAndClearConfigFiles() {
        if (providers.isEmpty) {
            return
        }
        val resourceFile = getLocation()
        try {
            logger.info("${supportAnnotation()} working on resource file: $resourceFile")
            val dependencies = Dependencies(true, *providers.values().map { it.second }.toTypedArray())
            generator.createNewFile(dependencies, "", resourceFile, "").bufferedWriter().use { writer ->
                for (providerInterface in providers.keySet()) {
                    val allServices = providers[providerInterface].map { it.first }.toSortedSet()
                    logger.info("${supportAnnotation()} new service file contents: $allServices")
                    writer.write("$providerInterface=\\")
                    writer.newLine()
                    for ((index, service) in allServices.withIndex()) {
                        writer.write(service)
                        if (index != allServices.size - 1) {
                            writer.write(",\\")
                        }
                        writer.newLine()
                    }
                    writer.newLine()
                }
            }
            logger.info("${supportAnnotation()} wrote to: $resourceFile")
        } catch (e: IOException) {
            logger.error("${supportAnnotation()} unable to create $resourceFile, $e")
        } finally {
            // 无论生成成功还是失败都清空，避免残留状态被带入下一处理轮次
            providers.clear()
        }
    }
}
