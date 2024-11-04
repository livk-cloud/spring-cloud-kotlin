package com.livk.function

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

/**
 * @author livk
 */
@SpringBootApplication
open class FunctionApp {
    @Bean
    open fun livk(): (String) -> Any = String::reversed
}

fun main(args: Array<String>) {
    runApplication<FunctionApp>(*args)
}
