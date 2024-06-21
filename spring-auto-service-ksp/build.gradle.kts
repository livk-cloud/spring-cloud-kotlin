plugins {
    com.livk.common
    id("com.google.devtools.ksp")
}

dependencies {
    api(project(":google-auto-service-ksp"))

    optional("com.google.auto.service:auto-service")
    ksp(project(":google-auto-service-ksp"))
}
