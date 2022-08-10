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
        (project.tasks.getByName(JavaPlugin.JAR_TASK_NAME) as Jar)
            .manifest.attributes(
                mapOf(
                    "Implementation-Group" to project.group,
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version,
                    "Created-By" to System.getProperty("java.version") + " (" + (System.getProperty("java.specification.vendor")) + ")",
                    "Gradle-Version" to GradleVersion.current()
                )
            )
    }
}
