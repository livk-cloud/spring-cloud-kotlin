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
                    mavenInfo(publication, project)
                }
            }
        }
        project.plugins.withType(JavaPlatformPlugin::class.java).all {
            project.components
                .matching { it.name.equals("javaPlatform") }
                .all { publication.from(it) }
            mavenInfo(publication, project)
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

    private fun mavenInfo(publication: MavenPublication, project: Project) {
        publication.pom {
            it.name.set(project.name)
            it.description.set(project.description)
            it.url.set("https://github.com/livk-cloud/spring-boot-example/" + project.name)
            it.licenses {
                it.license {
                    it.name.set("The Apache License, Version 2.0")
                    it.url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            it.developers {
                it.developer {
                    it.name.set("livk")
                    it.email.set("1375632510@qq.com")
                }
            }
            it.scm {
                it.connection.set("git@github.com:livk-cloud/spring-boot-example.git")
                it.url.set("https://github.com/livk-cloud/spring-boot-example")
            }
        }
    }
}
