package com.livk.swagger;

import com.livk.swagger.config.WebFluxSwaggerConfig;
import com.livk.swagger.support.GatewaySwaggerProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * LivkSwaggerAutoConfig
 * </p>
 *
 * @author livk
 * @date 2021/12/28
 */
@Configuration(proxyBeanMethods = false)
public class LivkSwaggerAutoConfig {

    @Bean
    public WebFluxSwaggerConfig webFluxSwaggerConfig() {
        return new WebFluxSwaggerConfig();
    }

    @Bean
    @ConditionalOnBean({RouteDefinitionRepository.class, DiscoveryClient.class})
    public GatewaySwaggerProvider gatewaySwaggerProvider(RouteDefinitionRepository routeDefinitionRepository,
                                                         DiscoveryClient discoveryClient) {
        return new GatewaySwaggerProvider(routeDefinitionRepository, discoveryClient);
    }
}
