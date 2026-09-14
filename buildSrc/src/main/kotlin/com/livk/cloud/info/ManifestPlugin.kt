package com.livk.cloud.info

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.util.GradleVersion

/**
 * <p>
 * ManifestPlugin
 * </p>
 *
 * @author livk
 * @date 2022/7/7
 */
abstract class ManifestPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(JavaPlugin::class.java)
        project.tasks.withType(Jar::class.java) {
            manifest {
                attributes["Implementation-Group"] = project.group
                attributes["Implementation-Title"] = project.name
                attributes["Implementation-Version"] = project.version
                attributes["Created-By"] =
                    "${System.getProperty("java.version")} (${System.getProperty("java.specification.vendor")})"
                attributes["Gradle-Version"] = GradleVersion.current()
            }
        }
    }
}
