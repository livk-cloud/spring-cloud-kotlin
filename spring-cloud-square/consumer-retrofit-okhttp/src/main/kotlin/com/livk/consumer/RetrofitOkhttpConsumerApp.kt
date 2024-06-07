package com.livk.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author livk
 */
@SpringBootApplication
open class RetrofitOkhttpConsumerApp

fun main(args: Array<String>) {
    runApplication<RetrofitOkhttpConsumerApp>(*args)
}
