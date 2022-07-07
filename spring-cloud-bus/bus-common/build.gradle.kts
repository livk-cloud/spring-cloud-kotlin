dependencies {
    api(project(":spring-common"))
    api("org.springframework.cloud:spring-cloud-starter-bus-stream")
//    api("org.springframework.cloud:spring-cloud-starter-bus-amqp")
    api("org.springframework.cloud:spring-cloud-starter-bus-kafka")
    api("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    api("org.springframework.cloud:spring-cloud-starter-consul-config")
    api("org.springframework.cloud:spring-cloud-starter-bootstrap")
    api("org.springframework.boot:spring-boot-starter-web")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
