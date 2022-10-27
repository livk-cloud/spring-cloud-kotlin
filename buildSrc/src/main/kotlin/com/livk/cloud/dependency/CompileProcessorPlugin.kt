package com.livk.cloud.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * CompileProcessorPlugin
 * </p>
 *
 * @author livk
 * @date 2022/8/10
 */
abstract class CompileProcessorPlugin :Plugin<Project>{

    companion object {
        const val COMPILE_PROCESSOR = "compileProcessor"

        val DEPENDENCY_NAMES_SET = HashSet<String>()

        init {
            DEPENDENCY_NAMES_SET.addAll(
                setOf(
                    JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME,
                    JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME,
                    JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME,
                    JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME
                )
            )
        }
    }

    override fun apply(project: Project) {
        val configurations = project.configurations
        project.pluginManager.apply(JavaPlugin::class.java)
        configurations.create(COMPILE_PROCESSOR) { compileProcessor ->
            compileProcessor.isVisible = false
            compileProcessor.isCanBeResolved = false
            compileProcessor.isCanBeConsumed = false
            val plugins = project.plugins
            plugins.withType(JavaPlugin::class.java) {
                DEPENDENCY_NAMES_SET.forEach { configurations.getByName(it).extendsFrom(compileProcessor) }
            }
        }
    }
}
