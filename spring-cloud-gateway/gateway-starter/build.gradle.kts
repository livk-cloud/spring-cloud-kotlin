dependencies {
    optional("org.springframework.cloud:spring-cloud-gateway-server")
    optional("org.springframework:spring-webflux")
    api(project(":spring-common"))
    api("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    api("io.springfox:springfox-boot-starter")
    api("org.springframework.cloud:spring-cloud-commons")
    api("com.fasterxml.jackson.core:jackson-databind")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    api("org.bouncycastle:bcprov-jdk15on")
    api("com.github.ben-manes.caffeine:caffeine")
}
