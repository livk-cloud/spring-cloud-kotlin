package com.livk.dynamic

import com.livk.auto.service.annotation.SpringAutoService
import com.livk.dynamic.annotation.EnableDynamicGateway
import com.livk.dynamic.support.RedisHashRouteDefinitionRepository
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.data.redis.autoconfigure.DataRedisReactiveAutoConfiguration
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer

/**
 * @author livk
 */
@SpringAutoService(EnableDynamicGateway::class)
@AutoConfiguration(
    before = [GatewayAutoConfiguration::class, DataRedisReactiveAutoConfiguration::class],
)
open class DynamicGatewayAutoConfig {
    @Bean
    open fun redisRouteDefinitionWriter(redisTemplate: ReactiveRedisTemplate<String, Any>): RedisHashRouteDefinitionRepository =
        RedisHashRouteDefinitionRepository(redisTemplate)

    @Bean
    open fun redisTemplate(connectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, Any> {
        val serializer = JacksonJsonRedisSerializer(Any::class.java)
        val context = RedisSerializationContext.newSerializationContext<String, Any>()
            .key(RedisSerializer.string())
            .value(serializer)
            .hashKey(RedisSerializer.string())
            .hashValue(serializer)
            .build()
        return ReactiveRedisTemplate(connectionFactory, context)
    }
}
