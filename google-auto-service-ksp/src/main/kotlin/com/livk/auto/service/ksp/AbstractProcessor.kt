package com.livk.auto.service.ksp

import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Multimaps
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.isLocal
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.ClassName

/**
 * @author livk
 */
abstract class AbstractProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

    protected val generator = environment.codeGenerator

    protected val logger = environment.logger

    protected lateinit var resolver: Resolver

    protected val providers =
        Multimaps.synchronizedSetMultimap(LinkedHashMultimap.create<String, Pair<String, KSFile>>())


    final override fun process(resolver: Resolver): List<KSAnnotated> {
        this.resolver = resolver
        val annotationStr = supportAnnotation()
        val autoServiceType =
            resolver.getClassDeclarationByName(resolver.getKSNameFromString(annotationStr))?.asType(emptyList())
                ?: run {
                    logger.info("@${annotationStr} type not found on the classpath, skipping processing.")
                    return emptyList()
                }
        for (symbolAnnotation in resolver.getSymbolsWithAnnotation(annotationStr)
            .filterIsInstance<KSClassDeclaration>()) {
            for (annotation in symbolAnnotation.annotations.filter { it.annotationType.resolve() == autoServiceType }) {
                accept(annotation, symbolAnnotation)
            }
        }
        generateAndClearConfigFiles()
        return emptyList()
    }

    protected abstract fun supportAnnotation(): String

    protected abstract fun accept(annotation: KSAnnotation, symbolAnnotation: KSClassDeclaration);

    protected abstract fun generateAndClearConfigFiles()

    protected fun KSClassDeclaration.toBinaryName(): String {
        return toClassName().reflectionName()
    }

    protected fun KSDeclaration.closestClassDeclarationBinaryName(): String {
        return closestClassDeclaration()!!.toBinaryName()
    }

    protected fun getArgument(annotation: KSAnnotation, key: String): Any? =
        annotation.arguments.find { it.name?.getShortName() == key }!!.value

    private fun KSClassDeclaration.toClassName(): ClassName {
        require(!isLocal()) { "Local/anonymous classes are not supported!" }
        val pkgName = packageName.asString()
        val typesString = qualifiedName!!.asString().removePrefix("$pkgName.")

        val simpleNames = typesString.split(".")
        return ClassName(pkgName, simpleNames)
    }
}
