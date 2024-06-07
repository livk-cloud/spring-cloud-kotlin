package com.livk.provider.api

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.livk.auto.service.annotation.SpringAutoService
import com.livk.provider.api.feign.factory.UserRemoteServiceFallbackFactory
import feign.Feign
import feign.Logger
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.cache.CacheManager
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * @author livk
 */
@SpringAutoService
@AutoConfiguration(before = [FeignAutoConfiguration::class])
@ConditionalOnClass(Feign::class)
open class OpenFeignConfig {
    @Bean
    open fun feignLoggerLevel(): Logger.Level {
        System.setProperty("logging.level.com.livk.provider.api.feign", "debug")
        return Logger.Level.FULL
    }

    @Bean
    open fun cacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        var redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        val mapper = ObjectMapper()
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        val serializer = Jackson2JsonRedisSerializer(mapper, Any::class.java)
        redisCacheConfiguration = redisCacheConfiguration.disableCachingNullValues()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer())
            )
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
            .cacheDefaults(redisCacheConfiguration).build()
    }

    @Bean
    open fun userRemoteServiceFallbackFactory(): UserRemoteServiceFallbackFactory {
        return UserRemoteServiceFallbackFactory()
    }
}
