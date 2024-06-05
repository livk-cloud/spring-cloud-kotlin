package com.livk.auto.service.ksp

import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Multimaps
import com.google.common.collect.Sets
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType
import java.io.IOException
import java.util.SortedSet

/**
 * @author livk
 */
class SpringAutoServiceProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return SpringAutoServiceProcessor(environment)
    }

    internal class SpringAutoServiceProcessor(environment: SymbolProcessorEnvironment) :
        AbstractProcessor(environment) {

        private val providers =
            Multimaps.synchronizedSetMultimap(LinkedHashMultimap.create<String, Pair<String, KSFile>>())

        override fun supportAnnotation(): String = "com.livk.auto.service.annotation.SpringAutoService"

        override fun processAnnotations(autoServiceType: KSType, symbolAnnotations: Sequence<KSClassDeclaration>) {
            for (symbolAnnotation in symbolAnnotations) {
                for (annotation in symbolAnnotation.annotations.filter { it.annotationType.resolve() == autoServiceType }) {
                    val implService = getArgument(annotation, "value") as KSType
                    var providerName = implService.declaration.closestClassDeclaration()?.toBinaryName()
                    if (providerName == Annotation::class.qualifiedName) {
                        providerName = "org.springframework.boot.autoconfigure.AutoConfiguration"
                    }
                    providers.put(providerName, symbolAnnotation.toBinaryName() to symbolAnnotation.containingFile!!)
                }
            }
        }

        override fun generateAndClearConfigFiles() {
            if (!providers.isEmpty) {
                for (providerInterface in providers.keySet()) {
                    val resourceFile = "META-INF/spring/${providerInterface}.imports"
                    logger.info("Working on resource file: $resourceFile")
                    try {
                        val allServices: SortedSet<String> = Sets.newTreeSet()
                        val foundImplementers = providers[providerInterface]
                        val newServices: Set<String> = HashSet(foundImplementers.map { it.first })
                        allServices.addAll(newServices)
                        logger.info("New service file contents: $allServices")
                        val ksFiles = foundImplementers.map { it.second }
                        logger.info("Originating files: ${ksFiles.map(KSFile::fileName)}")
                        val dependencies = Dependencies(true, *ksFiles.toTypedArray())
                        generator.createNewFile(dependencies, "", resourceFile, "").bufferedWriter().use { writer ->
                            for (service in allServices) {
                                writer.write(service)
                                writer.newLine()
                            }
                        }
                        logger.info("Wrote to: $resourceFile")
                    } catch (e: IOException) {
                        logger.error("Unable to create $resourceFile, $e")
                    }
                }
                providers.clear()
            }
        }
    }
}
