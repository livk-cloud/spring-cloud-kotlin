import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    maven("https://plugins.gradle.org/m2/")
    maven("https://repo.spring.io/release")
    maven("https://maven.aliyun.com/repository/public")
}

dependencies {
    implementation(libs.kotlin.jvm.plugin)
    implementation(libs.spring.boot.plugin)
    implementation(libs.kotlin.ksp.plugin)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        suppressWarnings = true
        allWarningsAsErrors = true
        jvmTarget = JvmTarget.JVM_21
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

tasks.withType<Jar> {
    manifest.attributes.putIfAbsent(
        "Created-By",
        System.getProperty("java.version") + " (" + System.getProperty("java.specification.vendor") + ")"
    )
    manifest.attributes.putIfAbsent("Gradle-Version", GradleVersion.current())
}
