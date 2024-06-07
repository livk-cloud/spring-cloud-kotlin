package com.livk.provider.biz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

/**
 * @author livk
 */
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
open class ProviderApp

fun main(args: Array<String>) {
    runApplication<ProviderApp>(*args)
}
