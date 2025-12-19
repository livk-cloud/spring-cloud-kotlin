plugins {
    com.livk.common
    id("com.google.devtools.ksp")
}

dependencies {
    optional("org.springframework.cloud:spring-cloud-gateway-server-webflux")
    optional("org.springframework.cloud:spring-cloud-loadbalancer")
    optional("org.springframework:spring-webflux")
    api(project(":spring-cloud-commons"))
    api("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    api("org.springframework.cloud:spring-cloud-commons")
    api("org.bouncycastle:bcprov-jdk18on")
    api("com.github.ben-manes.caffeine:caffeine")
    api("org.jetbrains.kotlinx:atomicfu")
    compileOnly("io.github.livk-cloud:spring-auto-service")
    ksp(project(":spring-auto-service-ksp"))
    testImplementation("com.squareup.okhttp3:mockwebserver")
    testImplementation("org.springframework.boot:spring-boot-webtestclient")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
}
