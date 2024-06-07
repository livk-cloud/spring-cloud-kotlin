package com.livk.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author livk
 */
@RestController
@SpringBootApplication
open class ConfigLocalClient {
    @Value("\${app.name}")
    lateinit var name: String


    @GetMapping("get")
    fun get(): HttpEntity<String> = ResponseEntity.ok(name)
}

fun main(args: Array<String>) {
    runApplication<ConfigLocalClient>(*args)
}
