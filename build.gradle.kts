plugins {
    com.livk.root
}

val bom = setOf(project(":dependencies"))
val gradleModuleProjects = subprojects.filter { it.buildFile.exists() } - bom

configure(gradleModuleProjects) {
    apply(plugin = "com.livk.module")

    dependencies {
        management(platform(project(":dependencies")))
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework:spring-tx")

        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("reflect"))
        testImplementation(kotlin("test"))
    }
}

allprojects {
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
    }
}
