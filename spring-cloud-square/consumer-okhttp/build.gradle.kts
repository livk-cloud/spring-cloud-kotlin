plugins {
    com.livk.service
}

dependencies {
    implementation(project(":spring-cloud-square:square-commons"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-square-okhttp")
}
