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

ext {
    set("bom", bom)
    set("gradleModuleProjects", gradleModuleProjects)
    set("commonModuleProjects", commonModuleProjects)
    set("springModuleProjects", springModuleProjects)
}

configure(bom) {
    apply(plugin = "com.livk.bom")
}

configure(commonModuleProjects) {
    apply(plugin = "com.livk.common")
}

configure(springModuleProjects) {
    apply(plugin = "com.livk.service")

    dependencies {
        implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
        implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
        optional("org.springframework.boot:spring-boot-devtools")
    }
}

configure(gradleModuleProjects) {
    apply(plugin = "com.livk.module")

    dependencies {
        management(platform(project(":livk-cloud-dependencies")))
        provider("org.projectlombok:lombok")
        provider("org.springframework.boot:spring-boot-configuration-processor")
        provider("org.springframework.boot:spring-boot-autoconfigure-processor")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework:spring-tx")
    }
}

configure(allprojects) {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://plugins.gradle.org/m2/") }
        maven { setUrl("https://repo.spring.io/release") }
    }
}
