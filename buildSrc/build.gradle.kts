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
val kotlinVersion: String = libs.versions.kotlin.get()

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:$bootVersion")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

gradlePlugin {
    plugins {
        create("bomPlugin") {
            id = "com.livk.bom"
            implementationClass = "com.livk.cloud.BomPlugin"
        }
        create("modulePlugin") {
            id = "com.livk.module"
            implementationClass = "com.livk.cloud.ModulePlugin"
        }
        create("commonPlugin") {
            id = "com.livk.common"
            implementationClass = "com.livk.cloud.CommonPlugin"
        }
        create("rootProjectPlugin") {
            id = "com.livk.root"
            implementationClass = "com.livk.cloud.RootProjectPlugin"
        }
        create("servicePlugin") {
            id = "com.livk.service"
            implementationClass = "com.livk.cloud.ServicePlugin"
        }
    }
}
