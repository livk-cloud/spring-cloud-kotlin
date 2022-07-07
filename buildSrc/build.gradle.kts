plugins {
    id("groovy-gradle-plugin")
}

repositories {
    maven { setUrl("https://maven.aliyun.com/repository/public") }
    maven { setUrl("https://plugins.gradle.org/m2/") }
    maven { setUrl("https://repo.spring.io/release") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.1")
}

gradlePlugin {
    plugins {
        create("compileArgsPlugin") {
            id = "com.livk.compile-args"
            implementationClass = "com.livk.compile.CompileArgsPlugin"
            description = "Add compile parameters"
        }
        create("dependencyBomPlugin") {
            id = "com.livk.dependency"
            implementationClass = "com.livk.DependencyBomPlugin"
            description = "build dependency BOM"
        }
        create("resourcesPlugin") {
            id = "com.livk.resources"
            implementationClass = "com.livk.compile.ResourcesPlugin"
            description = "Add resource handling"
        }
        create("deployedPlugin") {
            id = "com.livk.maven.deployed"
            implementationClass = "com.livk.maven.DeployedPlugin"
        }
        create("bootPlugin") {
            id = "com.livk.build.boot"
            implementationClass = "com.livk.jar.BootPlugin"
        }
    }
}
