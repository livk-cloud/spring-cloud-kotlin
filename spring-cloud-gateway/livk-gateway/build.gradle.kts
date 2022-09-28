dependencies {
    implementation(project(":spring-cloud-gateway:gateway-starter"))
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    testImplementation("com.github.tomakehurst:wiremock")
}
