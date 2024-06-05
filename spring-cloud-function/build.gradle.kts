plugins {
    com.livk.service
}

dependencies {
    implementation(project(":spring-cloud-commons"))
    implementation("org.springframework.cloud:spring-cloud-starter-function-web")
    implementation("org.springframework.boot:spring-boot-starter-web")
}
