package com.livk.cloud

import com.livk.cloud.compile.CompileArgsPlugin
import com.livk.cloud.info.BuildInfoPlugin
import com.livk.cloud.dependency.ManagementPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * ServicePlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/10
 */
class ServicePlugin :Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(JavaPlugin::class.java)
        project.pluginManager.apply(ModulePlugin::class.java)
        project.pluginManager.apply(CompileArgsPlugin::class.java)
        project.pluginManager.apply(BuildInfoPlugin::class.java)
    }
}
