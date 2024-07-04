package com.livk.factory.gray

import com.livk.auto.service.annotation.SpringAutoService
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledFilter
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory
import org.springframework.context.annotation.Bean

/**
 * @author livk
 */
@AutoConfiguration
@SpringAutoService
open class FactoryConfig {
    @Bean
    @ConditionalOnEnabledFilter
    open fun grayGatewayFilterFactory(clientFactory: LoadBalancerClientFactory): GrayGatewayFilterFactory =
        GrayGatewayFilterFactory(clientFactory)
}
