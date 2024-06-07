package com.livk.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author livk
 */
@SpringBootApplication
open class LivkGateway

fun main(args: Array<String>) {
    runApplication<LivkGateway>(*args)
}
