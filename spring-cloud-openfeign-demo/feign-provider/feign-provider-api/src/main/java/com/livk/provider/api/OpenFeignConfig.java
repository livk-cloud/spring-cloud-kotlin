package com.livk.provider.api;

import feign.Feign;
import feign.Logger;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * OpenFeignConfig
 * </p>
 *
 * @author livk
 * @date 2021/12/7
 */
@ComponentScan(basePackageClasses = OpenFeignConfig.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class OpenFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public okhttp3.OkHttpClient okHttpClient(OkHttpClientFactory okHttpClientFactory,
                                             FeignHttpClientProperties httpClientProperties) {
        return okHttpClientFactory.createBuilder(httpClientProperties.isDisableSslValidation())
                .connectTimeout(httpClientProperties.getConnectionTimeout(), TimeUnit.SECONDS)
                .followRedirects(httpClientProperties.isFollowRedirects())
                .build();
    }
}
