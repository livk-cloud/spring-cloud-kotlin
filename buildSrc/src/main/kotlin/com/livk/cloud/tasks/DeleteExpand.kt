package com.livk.cloud.tasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * <p>
 * DeleteExt
 * </p>
 *
 * @author livk
 * @date 2022/7/11
 */
abstract class DeleteExpand : Plugin<Project> {

    companion object {
        private val CLEAN_ALL_TASK_NAME = "cleanAll"
    }

    override fun apply(project: Project) {
        project.tasks.withType(Delete::class.java) {
            it.delete(project.projectDir.absolutePath + "/build")
            it.delete(project.projectDir.absolutePath + "/out")
            it.delete(project.projectDir.absolutePath + "/bin")
            it.delete(project.projectDir.absolutePath + "/src/main/generated")
            it.delete(project.projectDir.absolutePath + "/src/test/generated_tests")
        }
    }
}
