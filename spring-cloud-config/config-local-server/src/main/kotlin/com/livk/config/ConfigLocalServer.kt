package com.livk.config

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

/**
 * @author livk
 */
@EnableConfigServer
@SpringBootApplication
open class ConfigLocalServer

fun main(args: Array<String>) {
    runApplication<ConfigLocalServer>(*args)
}
