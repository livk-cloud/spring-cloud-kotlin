package com.livk.consumer

import okhttp3.OkHttpClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author livk
 */
@Configuration
open class OkhttpClientConfig {
    @Bean
    @LoadBalanced
    open fun okhttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()
}
