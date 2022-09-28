package com.livk.factory.gray;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledFilter;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * FactoryConfig
 * </p>
 *
 * @author livk
 * @date 2022/9/27
 */
@AutoConfiguration
public class FactoryConfig {

    @Bean
    @ConditionalOnEnabledFilter
    public GrayGatewayFilterFactory grayGatewayFilterFactory(LoadBalancerClientFactory clientFactory) {
        return new GrayGatewayFilterFactory(clientFactory);
    }
}
