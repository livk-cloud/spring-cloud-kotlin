package com.livk.cloud

import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * @author livk
 */
abstract class DependencyExtension {

    private var dependencies: DependencyHandler? = null

    fun setDependencies(dependencies: DependencyHandler?) {
        this.dependencies = dependencies
    }

    fun getDependencies(): DependencyHandler? {
        return dependencies
    }
}
