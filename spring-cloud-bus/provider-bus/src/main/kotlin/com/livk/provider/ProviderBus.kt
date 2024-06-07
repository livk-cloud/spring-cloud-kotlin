package com.livk.provider

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

/**
 * @author livk
 */
@SpringBootApplication
@EnableDiscoveryClient
open class ProviderBus

fun main(args: Array<String>) {
    runApplication<ProviderBus>(*args)
}
