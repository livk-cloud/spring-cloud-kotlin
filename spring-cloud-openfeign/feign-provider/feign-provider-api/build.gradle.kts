dependencies {
    api(project(":spring-cloud-commons"))
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    api("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    api("io.github.openfeign:feign-okhttp")
    api("org.springframework.boot:spring-boot-starter-data-redis")
}
