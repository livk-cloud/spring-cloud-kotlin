package com.livk.swagger;

import com.livk.swagger.config.WebFluxSwaggerConfig;
import com.livk.swagger.support.GatewaySwaggerProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * LivkSwaggerAutoConfig
 * </p>
 *
 * @author livk
 * @date 2021/12/28
 */
@AutoConfiguration
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
