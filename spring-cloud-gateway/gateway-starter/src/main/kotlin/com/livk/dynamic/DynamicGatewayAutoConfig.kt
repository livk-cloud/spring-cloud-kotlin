package com.livk.dynamic

import com.livk.auto.service.annotation.SpringAutoService
import com.livk.autoconfigure.redis.CustomizeRedisAutoConfiguration.CustomizeReactiveRedisAutoConfiguration
import com.livk.context.redis.ReactiveRedisOps
import com.livk.dynamic.annotation.EnableDynamicGateway
import com.livk.dynamic.support.RedisHashRouteDefinitionRepository
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration
import org.springframework.context.annotation.Bean

/**
 * @author livk
 */
@SpringAutoService(EnableDynamicGateway::class)
@AutoConfiguration(
    before = [GatewayAutoConfiguration::class, RedisAutoConfiguration::class],
    after = [CustomizeReactiveRedisAutoConfiguration::class]
)
open class DynamicGatewayAutoConfig {
    @Bean
    open fun redisRouteDefinitionWriter(reactiveRedisOps: ReactiveRedisOps): RedisHashRouteDefinitionRepository =
        RedisHashRouteDefinitionRepository(reactiveRedisOps)
}
