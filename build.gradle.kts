plugins {
    id("com.livk.dependency")
    id("org.springframework.boot") apply (false)
}

val bom = setOf(project(":livk-cloud-dependencies"), project(":livk-cloud-bom"))
val gradleModuleProjects = subprojects.filter {
    it.buildFile.exists()
}.toSet() - bom
val commonModuleProjects = gradleModuleProjects.filter {
    it.name.endsWith("-common")
            || it.name.endsWith("-starter")
            || it.name.endsWith("-api")
}.toSet()
val springModuleProjects = gradleModuleProjects - commonModuleProjects

configure(bom) {
    apply(plugin = "java-platform")
    apply(plugin = "com.livk.maven.deployed")

    project.extensions.getByType(JavaPlatformExtension::class.java).allowDependencies()
}

configure(commonModuleProjects) {
    apply(plugin = "com.livk.maven.deployed")
    apply(plugin = "com.livk.resources")
}

configure(springModuleProjects) {
    apply(plugin = "com.livk.build.boot")
}

configure(allprojects) {
    apply(plugin = "com.livk.clean.expand")
    apply(plugin = "com.livk.all.configuration")
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://plugins.gradle.org/m2/") }
        maven { setUrl("https://repo.spring.io/release") }
    }
}

configure(gradleModuleProjects) {
    apply(plugin = "com.livk.compile-args")
    apply(plugin = "com.livk.dependency")

    dependencies {
        dependencyBom(platform(project(":livk-cloud-dependencies")))
        annotationProcessor("org.projectlombok:lombok")

        compileOnly("org.projectlombok:lombok")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.projectlombok:lombok")

        testAnnotationProcessor("org.projectlombok:lombok")
    }
}
