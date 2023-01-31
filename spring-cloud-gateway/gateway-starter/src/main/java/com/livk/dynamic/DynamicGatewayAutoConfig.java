package com.livk.dynamic;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.dynamic.annotation.EnableDynamicGateway;
import com.livk.dynamic.support.LivkReactiveRedisTemplate;
import com.livk.dynamic.support.LivkRedisRouteDefinitionRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;

/**
 * <p>
 * DynamicGatewayAutoConfig
 * </p>
 *
 * @author livk
 * @date 2021/12/28
 */
@SpringAutoService(auto = EnableDynamicGateway.class)
@AutoConfiguration(before = {GatewayAutoConfiguration.class, RedisAutoConfiguration.class})
public class DynamicGatewayAutoConfig {

    @Bean
    public LivkReactiveRedisTemplate livkReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        return new LivkReactiveRedisTemplate(connectionFactory);
    }

    @Bean
    public LivkRedisRouteDefinitionRepository redisRouteDefinitionWriter(
            LivkReactiveRedisTemplate livkReactiveRedisTemplate) {
        return new LivkRedisRouteDefinitionRepository(livkReactiveRedisTemplate);
    }

}
