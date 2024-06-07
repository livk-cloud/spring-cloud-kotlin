package com.livk.commons.http

import com.livk.auto.service.annotation.SpringAutoService
import com.livk.commons.http.annotation.EnableRestClient
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.web.client.RestClientCustomizer
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer
import org.springframework.context.annotation.Bean

/**
 * @author livk
 */
@AutoConfiguration
@SpringAutoService(EnableRestClient::class)
@ConditionalOnBean(ReactiveLoadBalancer.Factory::class)
open class RestClientLoadBalanceConfiguration {
    @Bean
    open fun loadbalanceRestTemplateCustomizer(loadBalancerInterceptor: LoadBalancerInterceptor): RestClientCustomizer =
        RestClientCustomizer { builder -> builder.requestInterceptor(loadBalancerInterceptor) }
}
