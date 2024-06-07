package com.livk.consumer

import okhttp3.OkHttpClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.square.retrofit.EnableRetrofitClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author livk
 */
@Configuration
@EnableRetrofitClients
open class OkhttpClientConfig {
    @Bean
    @LoadBalanced
    open fun okHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()
}
