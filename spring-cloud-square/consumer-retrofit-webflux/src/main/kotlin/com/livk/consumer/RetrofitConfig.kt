package com.livk.consumer

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.square.retrofit.webclient.EnableRetrofitClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

/**
 * @author livk
 */
@Configuration
@EnableRetrofitClients
open class RetrofitConfig {
    @Bean
    @LoadBalanced
    open fun builder(): WebClient.Builder = WebClient.builder()
}
