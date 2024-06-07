package com.livk.consumer.biz

import com.livk.provider.api.feign.UserRemoteService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

/**
 * @author livk
 */
@EnableFeignClients(basePackageClasses = [UserRemoteService::class])
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
open class ConsumerApp

fun main(args: Array<String>) {
    runApplication<ConsumerApp>(*args)
}
