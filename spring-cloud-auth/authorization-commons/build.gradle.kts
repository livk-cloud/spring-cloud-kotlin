dependencies {
    api(project(":spring-cloud-commons"))
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.security:spring-security-oauth2-authorization-server")
    optional("jakarta.servlet:jakarta.servlet-api")
}
