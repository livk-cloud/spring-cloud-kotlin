package com.livk.commons.http

import com.livk.auto.service.annotation.SpringAutoService
import com.livk.commons.http.annotation.EnableWebClient
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.webclient.WebClientCustomizer
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerClientAutoConfiguration
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

/**
 *
 *
 * WebClientLoadBalanceConfiguration
 *
 *
 * @author livk
 */
@AutoConfiguration(after = [ReactorLoadBalancerClientAutoConfiguration::class])
@ConditionalOnClass(WebClient::class)
@ConditionalOnBean(
    ReactiveLoadBalancer.Factory::class
)
@SpringAutoService(EnableWebClient::class)
open class WebClientLoadBalanceConfiguration {
    @Bean
    open fun loadbalanceWebClientCustomizer(filterFunction: ReactorLoadBalancerExchangeFilterFunction): WebClientCustomizer =
        WebClientCustomizer { webClientBuilder: WebClient.Builder ->
            webClientBuilder.filter(
                filterFunction
            )
        }
}
