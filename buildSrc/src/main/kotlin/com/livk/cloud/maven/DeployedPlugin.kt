package com.livk.cloud.maven

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar

/**
 * <p>
 * DeployedPlugin
 * </p>
 *
 * @author livk
 * @date 2022/7/7
 */
abstract class DeployedPlugin : Plugin<Project> {
    companion object {
        const val NAME = "maven"
    }

    override fun apply(project: Project) {
        val publication = publication(project)
        project.afterEvaluate {
            project.plugins.withType(JavaPlugin::class.java).all {
                if ((project.tasks.getByName(JavaPlugin.JAR_TASK_NAME) as Jar).isEnabled) {
                    val javaPluginExtension = project.extensions.getByType(JavaPluginExtension::class.java)
                    javaPluginExtension.withSourcesJar()
                    project.components
                        .matching { it.name.equals("java") }
                        .all { publication.from(it) }
                }
            }
        }
        project.plugins.withType(JavaPlatformPlugin::class.java).all {
            project.components
                .matching { it.name.equals("javaPlatform") }
                .all { publication.from(it) }
        }
    }

    private fun publication(project: Project): MavenPublication {
        project.pluginManager.apply(MavenPublishPlugin::class.java)
        project.pluginManager.apply(MavenRepositoryPlugin::class.java)
        return project.extensions
            .getByType(PublishingExtension::class.java)
            .publications
            .create(NAME, MavenPublication::class.java)

    }
}
