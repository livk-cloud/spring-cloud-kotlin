package com.livk.consumer;

import okhttp3.OkHttpClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * OkhttpClientConfig
 * </p>
 *
 * @author livk
 * @date 2022/4/13
 */
@Configuration
public class OkhttpClientConfig {

    @Bean
    @LoadBalanced
    public OkHttpClient.Builder okhttpClientBuilder() {
        return new OkHttpClient.Builder();
    }
}
