package com.livk.cloud.maven

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
        publishing.repositories.maven { maven ->
            val releasesRepoUrl = project.property("mvn.releasesRepoUrl") as String
            val snapshotsRepoUrl = project.property("mvn.releasesRepoUrl") as String
            //使用不安全的http请求、也就是缺失SSL
            maven.isAllowInsecureProtocol = true
            val url = if (project.version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            maven.setUrl(url)
            maven.credentials {
                it.username = project.property("mvn.username") as String
                it.password = project.property("mvn.password") as String
            }
        }
    }
}
