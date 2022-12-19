package com.livk.cloud

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.bundling.Jar

/**
 * <p>
 * RootProjectPlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/10
 */
class RootProjectPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(BasePlugin::class.java)
        project.pluginManager.apply(CorePlugin::class.java)

        project.tasks.withType(Jar::class.java){
            it.enabled = false
        }
    }
}
