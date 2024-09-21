package com.livk.consumer

import com.livk.bus.listener.EnableRemoteEventListener
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment

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
open class ConsumerBus {

    @Bean
    open fun env(env: Environment): ApplicationRunner {
        return ApplicationRunner {
            println(env)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ConsumerBus>(*args)
}
