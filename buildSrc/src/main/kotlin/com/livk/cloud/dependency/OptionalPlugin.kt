package com.livk.cloud.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.internal.impldep.org.bouncycastle.oer.OERDefinition.optional

/**
 * <p>
 * OptionalPlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/15
 */
abstract class OptionalPlugin : Plugin<Project> {
    companion object {
        const val OPTIONAL = "optional"
    }

    override fun apply(project: Project) {
        val configurations = project.configurations
        project.pluginManager.apply(JavaPlugin::class.java)
        configurations.create(OPTIONAL) {
            isCanBeResolved = false
            isCanBeConsumed = false
            project.plugins.withType(JavaPlugin::class.java) {
                project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.forEach { sourceSet ->
                    configurations.getByName(sourceSet.compileClasspathConfigurationName).extendsFrom(this@create)
                    configurations.getByName(sourceSet.runtimeClasspathConfigurationName).extendsFrom(this@create)
                }
            }
        }
    }
}
