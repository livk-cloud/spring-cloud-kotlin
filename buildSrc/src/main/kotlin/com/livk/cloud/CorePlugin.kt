package com.livk.cloud

import com.livk.cloud.dependency.CompileProcessorPlugin
import com.livk.cloud.dependency.ManagementPlugin
import com.livk.cloud.dependency.OptionalPlugin
import com.livk.cloud.info.ManifestPlugin
import com.livk.cloud.tasks.DeleteExpand
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * <p>
 * CorePlugin
 * </p>
 *
 * @author livk
 * @date 2022/12/19
 */
class CorePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(DeleteExpand::class.java)
        project.pluginManager.apply(ManagementPlugin::class.java)
        project.pluginManager.apply(OptionalPlugin::class.java)
        project.pluginManager.apply(CompileProcessorPlugin::class.java)
        project.pluginManager.apply(ManifestPlugin::class.java)
    }

}
