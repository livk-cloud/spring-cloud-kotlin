import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version ("1.7.21")
}

repositories {
    maven { setUrl("https://maven.aliyun.com/repository/public") }
    maven { setUrl("https://plugins.gradle.org/m2/") }
    maven { setUrl("https://repo.spring.io/release") }
}

dependencies {
    implementation(libs.kotlin.jvm.plugin)
    implementation(libs.spring.boot.plugin)
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
