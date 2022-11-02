dependencies {
    implementation(project(":spring-cloud-auth:authorization-common"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    implementation("com.mysql:mysql-connector-j")
}
