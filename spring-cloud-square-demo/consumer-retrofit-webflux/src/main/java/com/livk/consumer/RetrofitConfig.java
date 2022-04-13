package com.livk.consumer;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.square.retrofit.webclient.EnableRetrofitClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <p>
 * RetrofitConfig
 * </p>
 *
 * @author livk
 * @date 2022/4/13
 */
@Configuration
@EnableRetrofitClients
public class RetrofitConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder builder() {
        return WebClient.builder();
    }
}
