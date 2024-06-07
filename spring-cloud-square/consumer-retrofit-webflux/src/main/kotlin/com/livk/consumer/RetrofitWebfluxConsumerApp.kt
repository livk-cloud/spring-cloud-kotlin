package com.livk.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author livk
 */
@SpringBootApplication
open class RetrofitWebfluxConsumerApp

fun main(args: Array<String>) {
    runApplication<RetrofitWebfluxConsumerApp>(*args)
}
