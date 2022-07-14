import org.springframework.boot.gradle.plugin.SpringBootPlugin

dependencies {
    api(platform(SpringBootPlugin.BOM_COORDINATES))
    api(platform(libs.spring.cloud.dependencies))
    api(platform(libs.spring.cloud.square.dependencies))
    constraints {
        val versionCatalog = rootProject.extensions
            .getByType(VersionCatalogsExtension::class.java)
            .named("libs")
        versionCatalog.libraryAliases
            .filter { !it.endsWith("dependencies") && !it.endsWith("bom") }
            .map { versionCatalog.findLibrary(it).get().get() }
            .forEach { api(it) }
    }
}
