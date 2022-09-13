package com.livk.cloud

import com.livk.cloud.dependency.ManagementPlugin
import com.livk.cloud.dependency.OptionalPlugin
import com.livk.cloud.dependency.ProviderPlugin
import com.livk.cloud.tasks.DeleteExpand
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin

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
        project.pluginManager.apply(DeleteExpand::class.java)
        project.pluginManager.apply(ManagementPlugin::class.java)
        project.pluginManager.apply(OptionalPlugin::class.java)
        project.pluginManager.apply(ProviderPlugin::class.java)
    }
}
