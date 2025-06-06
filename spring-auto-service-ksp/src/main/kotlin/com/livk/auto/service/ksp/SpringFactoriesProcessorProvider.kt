package com.livk.auto.service.ksp

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * @author livk
 */
@AutoService(SymbolProcessorProvider::class)
class SpringFactoriesProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        SpringFactoriesProcessor(environment)

    private class SpringFactoriesProcessor(environment: SymbolProcessorEnvironment) :
        AbstractFactoriesProcessor(environment) {

        companion object {
            const val SPRING_LOCATION: String = "META-INF/spring.factories"
        }

        override fun supportAnnotation(): String = "com.livk.auto.service.annotation.SpringFactories"

        override fun getLocation(): String = SPRING_LOCATION
    }
}
