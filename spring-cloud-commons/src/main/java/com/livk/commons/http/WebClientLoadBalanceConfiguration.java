package com.livk.commons.http;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.commons.http.annotation.EnableWebClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * WebClientLoadBalanceConfiguration
 * </p>
 *
 * @author livk
 */
@AutoConfiguration
@SpringAutoService(auto = EnableWebClient.class)
@ConditionalOnBean(ReactiveLoadBalancer.Factory.class)
public class WebClientLoadBalanceConfiguration {
    @Bean
    @ConditionalOnBean(ReactorLoadBalancerExchangeFilterFunction.class)
    public WebClientCustomizer loadbalanceWebClientCustomizer(ReactorLoadBalancerExchangeFilterFunction reactorLoadBalancerExchangeFilterFunction) {
        return webClientBuilder -> webClientBuilder.filter(reactorLoadBalancerExchangeFilterFunction);
    }
}
