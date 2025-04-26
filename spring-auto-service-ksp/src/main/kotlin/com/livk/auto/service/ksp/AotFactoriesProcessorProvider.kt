package com.livk.auto.service.ksp

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * <p>
 * AotFactoriesProcessorProvider
 * </p>
 *
 * @author livk
 * @date 2025/4/26
 */
@AutoService(SymbolProcessorProvider::class)
class AotFactoriesProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        AotFactoriesProcessor(environment)

    private class AotFactoriesProcessor(environment: SymbolProcessorEnvironment) :
        AbstractFactoriesProcessor(environment) {

        companion object {
            const val AOT_LOCATION: String = "META-INF/spring/aot.factories"
        }

        override fun supportAnnotation(): String = "com.livk.auto.service.annotation.AotFactories"

        override fun getLocation(): String = AOT_LOCATION
    }
}
