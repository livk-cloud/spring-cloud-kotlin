package com.livk.compile

import com.livk.info.ManifestPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile

/**
 * <p>
 * CompileArgsPlugin
 * </p>
 *
 * @author livk
 * @date 2022/7/7
 */
abstract class CompileArgsPlugin : Plugin<Project> {
    companion object {
        val COMPILER_ARGS = ArrayList<String>()
        val MAPSTRUCT_COMPILER_ARGS = ArrayList<String>()
        val MAPSTRUCT_NAME = "mapstruct"
        val MAPSTRUCT_PROCESSOR_NAME = "mapstruct-processor"
        val UTF_8 = "UTF-8"

        init {
            COMPILER_ARGS.addAll(
                listOf(
                    "-Xlint:-options",
                    "-Xlint:rawtypes",
                    "-Xlint:deprecation",
                    "-Xlint:unchecked"
                )
            )
            MAPSTRUCT_COMPILER_ARGS.addAll(listOf("-Amapstruct.unmappedTargetPolicy=IGNORE"))
        }
    }

    override fun apply(project: Project) {
        project.pluginManager.apply(JavaPlugin::class.java)
        project.pluginManager.apply(ManifestPlugin::class.java)
        val javaCompile = project.tasks
            .getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME) as JavaCompile
        javaCompile.options.compilerArgs.addAll(COMPILER_ARGS)
        javaCompile.options.encoding = UTF_8
        javaCompile.sourceCompatibility = JavaVersion.VERSION_17.toString()
        javaCompile.targetCompatibility = JavaVersion.VERSION_17.toString()

        project.afterEvaluate {
            val dependencyName = HashSet<String>()
            project.configurations.forEach {
                dependencyName.addAll(it.dependencies.map { dependency -> dependency.name })
            }
            if (dependencyName.contains(MAPSTRUCT_NAME) || dependencyName.contains(MAPSTRUCT_PROCESSOR_NAME)) {
                javaCompile.options.compilerArgs.addAll(MAPSTRUCT_COMPILER_ARGS)
            }
        }
    }
}
