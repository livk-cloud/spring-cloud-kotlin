val versionCatalog = rootProject.extensions
    .getByType(VersionCatalogsExtension::class.java)
    .named("libs")
val bom = versionCatalog.libraryAliases.filter { it.endsWith("dependencies") || it.endsWith("bom") }
val dependency = versionCatalog.libraryAliases.filter { !it.endsWith("plugin") } - bom.toSet()

dependencies {
    api(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    get(bom).forEach { api(platform(it)) }
    constraints {
        get(dependency).forEach { api(it) }
        (parent?.ext?.get("commonModuleProjects") as Collection<*>).filterIsInstance<Project>().forEach{api(it)}
    }
}

fun get(dependencyNames: Collection<String>): Collection<MinimalExternalModuleDependency> {
    return dependencyNames.map { versionCatalog.findLibrary(it).get().get() }
}
