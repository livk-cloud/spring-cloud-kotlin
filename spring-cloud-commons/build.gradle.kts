plugins {
    com.livk.common
    id("com.google.devtools.ksp")
}

dependencies {
    optional("org.springframework:spring-webflux")
    optional("org.springframework.boot:spring-boot-starter")
    optional("org.springframework.cloud:spring-cloud-commons")
    optional("org.springframework.boot:spring-boot-restclient")
    optional("org.springframework.boot:spring-boot-webclient")
    api("org.jetbrains.kotlin:kotlin-reflect")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    api("com.github.ben-manes.caffeine:caffeine")
    api("io.github.livk-cloud:spring-extension-commons")
    optional("org.springframework.cloud:spring-cloud-loadbalancer")

    compileOnly("io.github.livk-cloud:spring-auto-service")
    ksp(project(":spring-auto-service-ksp"))
}
