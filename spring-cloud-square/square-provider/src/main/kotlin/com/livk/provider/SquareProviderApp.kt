package com.livk.provider

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author livk
 */
@SpringBootApplication
open class SquareProviderApp

fun main(args: Array<String>) {
    runApplication<SquareProviderApp>(*args)
}
