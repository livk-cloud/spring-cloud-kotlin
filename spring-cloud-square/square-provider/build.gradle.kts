dependencies {
    implementation(project(":spring-cloud-square:square-common"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
