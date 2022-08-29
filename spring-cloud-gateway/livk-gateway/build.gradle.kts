dependencies {
    implementation(project(":spring-cloud-gateway:gateway-starter"))
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    testImplementation("com.github.tomakehurst:wiremock:2.27.2")
}
