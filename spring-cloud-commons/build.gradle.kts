plugins {
    com.livk.common
}

dependencies {
    optional("org.springframework:spring-webflux")
    optional("org.springframework.boot:spring-boot-starter")
    optional("org.springframework.cloud:spring-cloud-commons")
    api("org.jetbrains.kotlin:kotlin-reflect")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    api("com.github.ben-manes.caffeine:caffeine")
    api("io.github.livk-cloud:spring-extension-commons")
    compileProcessor("io.github.livk-cloud:spring-auto-service")
    optional("org.springframework.cloud:spring-cloud-loadbalancer")
}
