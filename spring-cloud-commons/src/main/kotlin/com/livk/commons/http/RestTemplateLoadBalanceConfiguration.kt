package com.livk.commons.http

import com.livk.auto.service.annotation.SpringAutoService
import com.livk.commons.http.annotation.EnableHttpClient
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer
import org.springframework.context.annotation.Bean
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestTemplate

/**
 * @author livk
 */
@AutoConfiguration
@SpringAutoService(EnableHttpClient::class)
@ConditionalOnBean(ReactiveLoadBalancer.Factory::class)
open class RestTemplateLoadBalanceConfiguration {
    @Bean
    @ConditionalOnMissingBean
    open fun restTemplateCustomizer(loadBalancerInterceptor: LoadBalancerInterceptor): RestTemplateCustomizer =
        RestTemplateCustomizer { restTemplate: RestTemplate ->
            val list: MutableList<ClientHttpRequestInterceptor> = ArrayList(restTemplate.interceptors)
            list.add(loadBalancerInterceptor)
            restTemplate.interceptors = list
        }
}
