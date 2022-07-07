dependencies {
    api(project(":spring-common"))
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    api("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    api("com.baomidou:mybatis-plus")
    api("io.github.openfeign:feign-okhttp")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
