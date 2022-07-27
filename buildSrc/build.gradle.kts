import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "1.7.10"
}

repositories {
    maven { setUrl("https://maven.aliyun.com/repository/public") }
    maven { setUrl("https://plugins.gradle.org/m2/") }
    maven { setUrl("https://repo.spring.io/release") }
}

val bootVersion: String = libs.versions.springBoot.get()

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:$bootVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

gradlePlugin {
    plugins {
        create("compileArgsPlugin") {
            id = "com.livk.compile-args"
            implementationClass = "com.livk.compile.CompileArgsPlugin"
            description = "Add compile parameters"
        }
        create("dependencyBomPlugin") {
            id = "com.livk.dependency"
            implementationClass = "com.livk.DependencyBomPlugin"
            description = "build dependency BOM"
        }
        create("resourcesPlugin") {
            id = "com.livk.resources"
            implementationClass = "com.livk.compile.ResourcesPlugin"
            description = "Add resource handling"
        }
        create("deployedPlugin") {
            id = "com.livk.maven.deployed"
            implementationClass = "com.livk.maven.DeployedPlugin"
        }
        create("bootPlugin") {
            id = "com.livk.build.boot"
            implementationClass = "com.livk.info.BootPlugin"
        }
        create("deleteExpand") {
            id = "com.livk.clean.expand"
            implementationClass = "com.livk.tasks.DeleteExpand"
        }
        create("allConfiguration") {
            id = "com.livk.all.configuration"
            implementationClass = "com.livk.config.AllConfiguration"
        }
    }
}
