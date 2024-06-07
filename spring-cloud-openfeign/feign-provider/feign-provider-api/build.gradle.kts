plugins {
    com.livk.common
    id("com.google.devtools.ksp")
}

dependencies {
    api(project(":spring-cloud-commons"))
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    api("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    api("io.github.openfeign:feign-okhttp")
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("io.micrometer:micrometer-tracing-bridge-otel")
    api("io.github.openfeign:feign-micrometer")
    api("org.springframework.boot:spring-boot-starter-actuator")
    compileOnly("io.github.livk-cloud:spring-auto-service")
    ksp(project(":spring-auto-service-ksp"))
}
