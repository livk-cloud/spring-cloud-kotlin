plugins {
    com.livk.bom
}
dependencies {
    api(platform(libs.spring.extension.dependencies))
    api(platform(libs.spring.cloud.dependencies))
    api(platform(libs.spring.cloud.square.dependencies))
    constraints {
        api(libs.bcprov.jdk15on)
        api(libs.wiremock)
    }
}
