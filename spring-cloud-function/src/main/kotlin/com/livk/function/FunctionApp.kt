package com.livk.function

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.function.Function

/**
 * @author livk
 */
@SpringBootApplication
open class FunctionApp {
    @Bean
    open fun livk(): Function<String, Any> = Function { value -> StringBuilder(value).reverse().toString() }
}

fun main(args: Array<String>) {
    runApplication<FunctionApp>(*args)
}
