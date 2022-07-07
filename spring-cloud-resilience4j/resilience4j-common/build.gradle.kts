dependencies {
    api("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
