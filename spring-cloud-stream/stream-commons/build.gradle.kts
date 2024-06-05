plugins {
    com.livk.common
}

dependencies {
    api(project(":spring-cloud-commons"))
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.cloud:spring-cloud-starter-consul-discovery")
}
