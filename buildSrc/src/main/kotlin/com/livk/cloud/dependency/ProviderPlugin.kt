package com.livk.cloud.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaTestFixturesPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * <p>
 * ProviderPlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/10
 */
abstract class ProviderPlugin :Plugin<Project>{

    companion object {
        const val PROVIDER = "provider"

        val DEPENDENCY_NAMES_SET = HashSet<String>()

        init {
            DEPENDENCY_NAMES_SET.addAll(
                setOf(
                    JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME,
                    JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME,
                    JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME,
                    JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME
                )
            )
        }
    }

    override fun apply(project: Project) {
        val configurations = project.configurations
        project.pluginManager.apply(JavaPlugin::class.java)
        configurations.create(PROVIDER) { provider ->
            provider.isVisible = false
            provider.isCanBeResolved = false
            provider.isCanBeConsumed = false
            val plugins = project.plugins
            plugins.withType(JavaPlugin::class.java) {
                DEPENDENCY_NAMES_SET.forEach { configurations.getByName(it).extendsFrom(provider) }
            }
        }
    }
}
