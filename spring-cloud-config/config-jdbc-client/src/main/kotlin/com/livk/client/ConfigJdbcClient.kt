package com.livk.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author livk
 */
@RestController
@SpringBootApplication
open class ConfigJdbcClient {
    @Value("\${foo}")
    lateinit var foo: String

    @RequestMapping(value = ["/foo"])
    fun hi(): String = foo
}

fun main(args: Array<String>) {
    runApplication<ConfigJdbcClient>(*args)
}
