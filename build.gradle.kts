plugins {
    com.livk.root
}

val bom = setOf(project(":dependencies"))
val gradleModuleProjects = subprojects.filter { it.buildFile.exists() } - bom
val commonModuleProjects = gradleModuleProjects.filter {
    it.name.endsWith("-ksp") || it.name.endsWith("-commons")
            || it.name.endsWith("-starter") || it.name.endsWith("-api")
}.toSet()
val springModuleProjects = gradleModuleProjects - commonModuleProjects

configure(bom) {
    apply(plugin = "com.livk.bom")
}

configure(commonModuleProjects) {
    apply(plugin = "com.livk.common")
}

configure(springModuleProjects) {
    apply(plugin = "com.livk.service")
}

configure(gradleModuleProjects) {
    apply(plugin = "com.livk.module")

    dependencies {
        management(platform(project(":dependencies")))
        compileProcessor("org.projectlombok:lombok")
        compileProcessor("org.springframework.boot:spring-boot-configuration-processor")
        compileProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework:spring-tx")
    }
}

configure(allprojects) {
    repositories {
        maven("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    }
}
