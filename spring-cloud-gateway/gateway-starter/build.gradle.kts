plugins {
    com.livk.common
    id("com.google.devtools.ksp")
}

dependencies {
    optional("org.springframework.cloud:spring-cloud-gateway-server")
    optional("org.springframework.cloud:spring-cloud-loadbalancer")
    optional("org.springframework:spring-webflux")
    api(project(":spring-cloud-commons"))
    api("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    api("org.springframework.cloud:spring-cloud-commons")
    api("com.fasterxml.jackson.core:jackson-databind")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    api("org.bouncycastle:bcprov-jdk18on")
    api("com.github.ben-manes.caffeine:caffeine")
    api("io.github.livk-cloud:redis-ops-boot-starter")
    api("org.jetbrains.kotlinx:atomicfu")
    compileOnly("io.github.livk-cloud:spring-auto-service")
    ksp(project(":spring-auto-service-ksp"))
}
