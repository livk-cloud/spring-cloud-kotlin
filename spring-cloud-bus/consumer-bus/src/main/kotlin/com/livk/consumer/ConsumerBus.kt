package com.livk.consumer

import com.livk.bus.listener.EnableRemoteEventListener
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

/**
 *
 *
 * ConsumerBus
 *
 *
 * @author livk
 */
@EnableDiscoveryClient
@EnableRemoteEventListener
@SpringBootApplication
open class ConsumerBus

fun main(args: Array<String>) {
    runApplication<ConsumerBus>(*args)
}
