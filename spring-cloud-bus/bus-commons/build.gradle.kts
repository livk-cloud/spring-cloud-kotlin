plugins {
    com.livk.common
    id("com.google.devtools.ksp")
}

dependencies {
    api(project(":spring-cloud-commons"))
    api("org.springframework.cloud:spring-cloud-starter-bus-stream")
//    api("org.springframework.cloud:spring-cloud-starter-bus-amqp")
    api("org.springframework.cloud:spring-cloud-starter-bus-kafka")
    api("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    api("org.springframework.cloud:spring-cloud-starter-consul-config")
    api("org.springframework.boot:spring-boot-starter-web")
    compileOnly("io.github.livk-cloud:spring-auto-service")
    ksp(project(":spring-auto-service-ksp"))
}
