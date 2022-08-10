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
            val releasesRepoUrl = "http://localhost:8081/repository/maven-releases/"
            val snapshotsRepoUrl = "http://localhost:8081/repository/maven-snapshots/"
            //使用不安全的http请求、也就是缺失SSL
            maven.isAllowInsecureProtocol = true
            maven.setUrl(if (project.version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            maven.credentials {
                it.username = "admin"
                it.password = "admin"
            }
        }
    }
}
