package com.livk.auto.service.ksp

import com.google.common.collect.LinkedHashMultimap
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

    /**
     * KSP 为每个模块编译只创建一个 processor 实例，且 [process] 在同一实例上是按轮次顺序调用的，
     * 不存在跨线程并发访问，因此这里无需线程安全的 Multimap 实现。
     */
    protected val providers = LinkedHashMultimap.create<String, Pair<String, KSFile>>()


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

    final override fun finish() {
        providers.clear()
    }

    protected abstract fun supportAnnotation(): String

    protected abstract fun accept(annotation: KSAnnotation, symbolAnnotation: KSClassDeclaration);

    /**
     * 每一轮 [process] 结束后都会被调用。实现类必须在生成完文件后清空 [providers]（建议用
     * try/finally 包裹，确保生成失败时也会清空），避免上一轮的数据被带入下一轮处理——KSP 只要
     * 某一轮生成了新文件就会再触发一轮处理，如果 [providers] 未被清空，下一轮会尝试对同一路径
     * 重复创建文件而抛出异常。
     *
     * 不同的注解对应不同的产物格式和语义（例如 ServiceLoader 的 META-INF/services 文件顺序无关，
     * 而 Spring 的 *.imports 文件顺序可能影响自动配置处理顺序），因此具体的生成规则由各实现类
     * 自行掌控，不在此处提供通用模板，避免不同产物格式的生成规则被意外耦合在一起。
     */
    protected abstract fun generateAndClearConfigFiles()

    protected fun KSClassDeclaration.toBinaryName(): String {
        return toClassName().reflectionName()
    }

    protected fun KSDeclaration.closestClassDeclarationBinaryName(): String {
        return closestClassDeclaration()!!.toBinaryName()
    }

    protected fun getArgumentValue(annotation: KSAnnotation): Any? =
        annotation.arguments.find { it.name?.getShortName() == "value" }!!.value

    private fun KSClassDeclaration.toClassName(): ClassName {
        require(!isLocal()) { "Local/anonymous classes are not supported!" }
        val pkgName = packageName.asString()
        val typesString = qualifiedName!!.asString().removePrefix("$pkgName.")

        val simpleNames = typesString.split(".")
        return ClassName(pkgName, simpleNames)
    }
}
