pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "spring-cloud-example"

fileTree(rootDir) {
    val excludes = gradle.startParameter.projectProperties["excludeProjects"]?.split(",")
    include("**/*.gradle.kts")
    exclude("build", "**/gradle", "settings.gradle.kts", "buildSrc", "/build.gradle.kts", ".", "out")
    if (!excludes.isNullOrEmpty()) {
        exclude(excludes)
    }
}.forEach {
    val buildFilePath = it.parentFile.absolutePath
    val projectPath = buildFilePath.replace(rootDir.absolutePath, "").replace(File.separator, ":")
    include(projectPath)

    val project = findProject(projectPath)
    if (project != null) {
        project.projectDir = it.parentFile
        project.buildFileName = it.name
    }
}

gradle.settingsEvaluated {
    if (JavaVersion.current() < JavaVersion.VERSION_21) {
        throw GradleException("This build requires JDK 21. It's currently ${JavaVersion.current()}. You can ignore this check by passing '-Dorg.gradle.ignoreBuildJavaVersionCheck'.")
    }
}
