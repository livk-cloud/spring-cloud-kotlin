plugins {
    com.livk.service
}

dependencies {
    implementation(project(":spring-cloud-openfeign:feign-provider:feign-provider-api"))
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    testImplementation(kotlin("test"))
}
repositories {
    mavenCentral()
}
