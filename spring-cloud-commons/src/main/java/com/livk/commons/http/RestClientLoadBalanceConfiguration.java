package com.livk.commons.http;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.commons.http.annotation.EnableHttpClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.context.annotation.Bean;

/**
 * @author livk
 */
@AutoConfiguration
@SpringAutoService(EnableHttpClient.class)
@ConditionalOnBean(ReactiveLoadBalancer.Factory.class)
public class RestClientLoadBalanceConfiguration {

    @Bean
    public RestClientCustomizer loadbalanceRestTemplateCustomizer(LoadBalancerInterceptor loadBalancerInterceptor) {
        return builder -> builder.requestInterceptor(loadBalancerInterceptor);
    }
}
