package com.livk.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

/**
 *
 *
 * ConfigServer
 *
 *
 * @author livk
 */
@EnableConfigServer
@SpringBootApplication
open class ConfigJdbcServer

fun main(args: Array<String>) {
    runApplication<ConfigJdbcServer>(*args)
}
