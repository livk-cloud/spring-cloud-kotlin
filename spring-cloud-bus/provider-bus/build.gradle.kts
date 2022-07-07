dependencies {
    implementation(project(":spring-cloud-bus:bus-common"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
