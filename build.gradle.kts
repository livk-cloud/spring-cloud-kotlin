plugins {
    com.livk.root
}

val bom = setOf(project(":livk-cloud-dependencies"))
val gradleModuleProjects = subprojects.filter {
    it.buildFile.exists()
}.toSet() - bom
val commonModuleProjects = gradleModuleProjects.filter {
    it.name.endsWith("-commons") || it.name.endsWith("-starter") || it.name.endsWith("-api")
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
        compileProcessor("org.projectlombok:lombok")
        compileProcessor("org.springframework.boot:spring-boot-configuration-processor")
        compileProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework:spring-tx")
    }
}

configure(allprojects) {
    repositories {
        maven { setUrl("https://plugins.gradle.org/m2/") }
        maven { setUrl("https://repo.spring.io/release") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven {
            setUrl("http://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
            isAllowInsecureProtocol = true
        }
    }
}
