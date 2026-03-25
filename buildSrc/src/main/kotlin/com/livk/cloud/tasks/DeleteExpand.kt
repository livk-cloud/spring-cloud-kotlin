package com.livk.cloud.tasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete

/**
 * <p>
 * DeleteExt
 * </p>
 *
 * @author livk
 * @date 2022/7/11
 */
abstract class DeleteExpand : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.withType(Delete::class.java) {
            delete(project.projectDir.absolutePath + "/build")
            delete(project.projectDir.absolutePath + "/out")
            delete(project.projectDir.absolutePath + "/bin")
            delete(project.projectDir.absolutePath + "/src/main/generated")
            delete(project.projectDir.absolutePath + "/src/test/generated_tests")
        }
    }
}
