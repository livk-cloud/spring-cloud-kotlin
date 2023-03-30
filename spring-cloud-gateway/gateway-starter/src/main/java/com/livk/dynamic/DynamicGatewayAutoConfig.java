package com.livk.dynamic;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.redis.supprot.UniversalReactiveRedisTemplate;
import com.livk.dynamic.annotation.EnableDynamicGateway;
import com.livk.dynamic.support.RedisHashRouteDefinitionRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * DynamicGatewayAutoConfig
 * </p>
 *
 * @author livk
 * @date 2021/12/28
 */
@SpringAutoService(EnableDynamicGateway.class)
@AutoConfiguration(before = {GatewayAutoConfiguration.class, RedisAutoConfiguration.class})
public class DynamicGatewayAutoConfig {

    @Bean
    public RedisHashRouteDefinitionRepository redisRouteDefinitionWriter(
            UniversalReactiveRedisTemplate reactiveRedisTemplate) {
        return new RedisHashRouteDefinitionRepository(reactiveRedisTemplate);
    }

}
