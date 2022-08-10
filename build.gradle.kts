plugins {
    id("com.livk.root")
}

val bom = setOf(project(":livk-cloud-dependencies"))
val gradleModuleProjects = subprojects.filter {
    it.buildFile.exists()
}.toSet() - bom
val commonModuleProjects = gradleModuleProjects.filter {
    it.name.endsWith("-common") || it.name.endsWith("-starter") || it.name.endsWith("-api")
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
        management(platform(project(":livk-cloud-dependencies")))
        provider("org.projectlombok:lombok")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework:spring-tx")
    }
}
