package com.livk.cloud

import com.livk.cloud.compile.CompileArgsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

/**
 * <p>
 * ModulePlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/10
 */
class ModulePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(KotlinPluginWrapper::class.java)
        project.pluginManager.apply(CompileArgsPlugin::class.java)
        project.pluginManager.apply(CorePlugin::class.java)
    }
}
