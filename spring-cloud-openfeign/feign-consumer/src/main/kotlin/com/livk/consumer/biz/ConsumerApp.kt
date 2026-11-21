package com.livk.consumer.biz

import com.livk.provider.api.feign.HttpUserRemoteService
import com.livk.provider.api.feign.UserRemoteService
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.web.service.registry.HttpServiceGroup
import org.springframework.web.service.registry.ImportHttpServices

/**
 * @author livk
 */
@ImportHttpServices(
    group = "feign-provider-biz",
    basePackageClasses = [HttpUserRemoteService::class],
    clientType = HttpServiceGroup.ClientType.REST_CLIENT
)
@EnableFeignClients(basePackageClasses = [UserRemoteService::class])
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
class ConsumerApp {
    @Bean
    fun runner(remoteService: HttpUserRemoteService): ApplicationRunner {
        return ApplicationRunner {
            remoteService.users().forEach { println(it) }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ConsumerApp>(*args)
}
