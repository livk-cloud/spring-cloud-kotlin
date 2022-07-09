package com.livk

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * DependencyBomPlugin
 * </p>
 *
 * @author livk
 * @date 2022/7/8
 */
abstract class DependencyBomPlugin : Plugin<Project> {
    companion object {
        const val DEPENDENCY_BOM = "dependencyBom"
    }

    override fun apply(project: Project) {
        project.pluginManager.apply(JavaPlugin::class.java)
        val dependencyBom = project.configurations.create(DEPENDENCY_BOM)
        dependencyBom.isVisible = false
        dependencyBom.isCanBeResolved = false
        dependencyBom.isCanBeConsumed = false
        project.plugins.withType(JavaPlugin::class.java) {
            project.configurations.getByName(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME).extendsFrom(dependencyBom)
            project.configurations.getByName(JavaPlugin.API_ELEMENTS_CONFIGURATION_NAME).extendsFrom(dependencyBom)
            project.configurations.getByName(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME).extendsFrom(dependencyBom)
            project.configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME).extendsFrom(dependencyBom)
            project.configurations.getByName(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME).extendsFrom(dependencyBom)
            project.configurations.getByName(JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME).extendsFrom(dependencyBom)
            project.configurations.getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME).extendsFrom(dependencyBom)
        }
    }
}
