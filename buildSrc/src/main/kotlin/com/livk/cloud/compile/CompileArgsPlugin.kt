package com.livk.cloud.compile

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * <p>
 * CompileArgsPlugin
 * </p>
 *
 * @author livk
 * @date 2026/3/25
 */
abstract class CompileArgsPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.withType(KotlinCompile::class.java) {
            compilerOptions {
                freeCompilerArgs.set(listOf("-Xjsr305=strict"))
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }

        project.tasks.withType(Test::class.java) {
            useJUnitPlatform()
        }
    }
}
