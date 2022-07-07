plugins {
    id("com.livk.dependency")
    id("org.springframework.boot") apply (false)
}

val bom = project(":livk-cloud-dependencies")
val gradleModuleProjects = subprojects.filter {
    it.buildFile.exists()
}.toSet() - bom
val commonModuleProjects = gradleModuleProjects.filter {
    it.name.endsWith("-common")
            || it.name.endsWith("-starter")
            || it.name.endsWith("-api")
}.toSet()
val springModuleProjects = gradleModuleProjects - commonModuleProjects

configure(commonModuleProjects) {
    apply(plugin = "com.livk.maven.deployed")
    apply(plugin = "com.livk.resources")
}

configure(springModuleProjects) {
    apply(plugin = "com.livk.build.boot")
}

configure(allprojects) {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://plugins.gradle.org/m2/") }
        maven { setUrl("https://repo.spring.io/release") }
    }
}

configure(gradleModuleProjects) {
    apply(plugin = "com.livk.compile-args")
    apply(plugin = "com.livk.dependency")

    tasks {
        getByName<Delete>(BasePlugin.CLEAN_TASK_NAME) {
            delete("$projectDir/build")
            delete("$projectDir/out")
            delete("$projectDir/bin")
        }
    }

    tasks.getByName<Test>(JavaPlugin.TEST_TASK_NAME) {
        useJUnitPlatform()
    }

    dependencies {
        dependencyBom(platform(project(":livk-cloud-dependencies")))
        annotationProcessor("org.projectlombok:lombok")

        compileOnly("org.projectlombok:lombok")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.projectlombok:lombok")

        testAnnotationProcessor("org.projectlombok:lombok")
    }
}
