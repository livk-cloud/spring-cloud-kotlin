package com.livk.maven

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * <p>
 * MavenRepositoryPlugin
 * </p>
 *
 * @author livk
 * @date 2022/7/7
 */
abstract class MavenRepositoryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(MavenPublishPlugin::class.java)
        val publishing = project.extensions.getByType(PublishingExtension::class.java)
        publishing.repositories.mavenLocal()
    }
}
