dependencies {
    api(platform(project(":livk-cloud-dependencies")))
    constraints {
        rootProject.allprojects
            .filter {
                it.name.endsWith("-common")
                        || it.name.endsWith("-starter")
                        || it.name.endsWith("-api")
            }.forEach { api(it) }
    }
}
