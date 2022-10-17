import org.springframework.boot.gradle.plugin.SpringBootPlugin
import java.util.function.Consumer

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
        commonModuleProjectConsumer { api(it) }
    }
}

fun commonModuleProjectConsumer(consumer: Consumer<Project>) {
    val commonModuleProjects = parent?.ext?.get("commonModuleProjects")
    if (commonModuleProjects is Collection<*>) {
        commonModuleProjects.filterIsInstance<Project>().forEach(consumer)
    }
}

fun get(dependencyNames: Collection<String>): Collection<MinimalExternalModuleDependency> {
    return dependencyNames.map { versionCatalog.findLibrary(it).get().get() }
}
