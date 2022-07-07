dependencies {
    implementation(project(":spring-cloud-stream:kafka-common"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
