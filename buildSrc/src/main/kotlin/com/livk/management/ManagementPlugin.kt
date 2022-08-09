package com.livk.management

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaTestFixturesPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * <p>
 * DependencyBomPlugin
 * </p>
 *
 * @author livk
 * @date 2022/7/8
 */
abstract class ManagementPlugin : Plugin<Project> {
    companion object {
        const val DEPENDENCY_BOM = "management"

        val DEPENDENCY_NAMES_SET = HashSet<String>()

        init {
            DEPENDENCY_NAMES_SET.addAll(
                setOf(
                    JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME,
                    JavaPlugin.API_ELEMENTS_CONFIGURATION_NAME,
                    JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME,
                    JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME,
                    JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME,
                    JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME,
                    JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME
                )
            )
        }
    }

    override fun apply(project: Project) {
        val configurations = project.configurations
        project.pluginManager.apply(JavaPlugin::class.java)
        configurations.create(DEPENDENCY_BOM) { dependencyBom ->
            dependencyBom.isVisible = false
            dependencyBom.isCanBeResolved = false
            dependencyBom.isCanBeConsumed = false
            val plugins = project.plugins
            plugins.withType(JavaPlugin::class.java) {
                DEPENDENCY_NAMES_SET.forEach { configurations.getByName(it).extendsFrom(dependencyBom) }
            }
            plugins.withType(JavaTestFixturesPlugin::class.java) {
                configurations.getByName("testFixturesCompileClasspath").extendsFrom(dependencyBom)
                configurations.getByName("testFixturesRuntimeClasspath").extendsFrom(dependencyBom)
            }
            plugins.withType(MavenPublishPlugin::class.java) {
                project.extensions
                    .getByType(PublishingExtension::class.java)
                    .publications
                    .withType(MavenPublication::class.java) { mavenPublication ->
                        mavenPublication.versionMapping { versions ->
                            versions.allVariants {
                                it.fromResolutionResult()
                            }
                        }
                    }
            }
        }
    }
}
