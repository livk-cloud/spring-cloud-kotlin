package com.livk.cloud

import com.livk.cloud.maven.DeployedPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlatformExtension
import org.gradle.api.plugins.JavaPlatformPlugin

/**
 * <p>
 * BomPlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/10
 */
class BomPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(JavaPlatformPlugin::class.java)
        project.pluginManager.apply(DeployedPlugin::class.java)

        project.extensions.getByType(JavaPlatformExtension::class.java).allowDependencies()
    }
}
