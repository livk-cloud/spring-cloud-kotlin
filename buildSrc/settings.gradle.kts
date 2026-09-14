dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

gradle.gradle.settingsEvaluated {
    if (JavaVersion.current() < JavaVersion.VERSION_25) {
        throw GradleException("This build requires JDK 25. It's currently ${JavaVersion.current()}. You can ignore this check by passing '-Dorg.gradle.ignoreBuildJavaVersionCheck'.")
    }
}
