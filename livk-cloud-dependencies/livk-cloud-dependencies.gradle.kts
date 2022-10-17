import org.springframework.boot.gradle.plugin.SpringBootPlugin

val versionCatalog = rootProject.extensions
    .getByType(VersionCatalogsExtension::class.java)
    .named("libs")
val bom = versionCatalog.libraryAliases.filter { it.endsWith("dependencies") || it.endsWith("bom") }
val dependency = versionCatalog.libraryAliases.filter { !it.endsWith("plugin") } - bom.toSet()

dependencies {
    api(platform(SpringBootPlugin.BOM_COORDINATES))
    get(bom).forEach { api(platform(it)) }
    constraints {
        get(dependency).forEach { api(it) }
        commonModuleProjectNames().forEach { api(it) }
    }
}

fun commonModuleProjectNames(): Collection<Project> {
    val commonModuleProjects = parent?.ext?.get("commonModuleProjects")
    if (commonModuleProjects is Collection<*>) {
        return commonModuleProjects.filterIsInstance<Project>()
    }
    return setOf()
}

fun get(dependencyNames: Collection<String>): Collection<MinimalExternalModuleDependency> {
    return dependencyNames.map { versionCatalog.findLibrary(it).get().get() }
}
