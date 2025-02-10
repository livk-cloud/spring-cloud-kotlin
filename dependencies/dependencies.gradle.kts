plugins {
    com.livk.bom
}
dependencies {
    api(platform(kotlin("bom", libs.versions.kotlin.asProvider().get())))
    api(platform(libs.spring.extension.dependencies))
    api(platform(libs.spring.cloud.dependencies))
    constraints {
        api(libs.bcprov.jdk15on)
        api(libs.wiremock)
        api(libs.symbol.processing)
        api(libs.kotlinpoet)
        api(libs.atomicfu)
    }
}
