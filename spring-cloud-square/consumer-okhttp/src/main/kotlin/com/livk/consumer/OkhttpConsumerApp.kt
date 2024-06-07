package com.livk.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author livk
 */
@SpringBootApplication
open class OkhttpConsumerApp

fun main(args: Array<String>) {
    runApplication<OkhttpConsumerApp>(*args)
}
