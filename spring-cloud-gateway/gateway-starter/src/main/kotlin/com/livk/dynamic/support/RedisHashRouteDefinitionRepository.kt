package com.livk.dynamic.support

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.route.RouteDefinition
import org.springframework.cloud.gateway.route.RouteDefinitionRepository
import org.springframework.cloud.gateway.support.NotFoundException
import org.springframework.data.redis.core.ReactiveHashOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * 使用Redis Hash存储路由信息
 * 也可以参考[org.springframework.cloud.gateway.route.RedisRouteDefinitionRepository]使用Redis Value存储
 * @author livk
 */
class RedisHashRouteDefinitionRepository(redisTemplate: ReactiveRedisTemplate<String, Any>) : RouteDefinitionRepository {
    private val caffeineCache: Cache<String, RouteDefinition>

    private val reactiveHashOperations: ReactiveHashOperations<String, String, RouteDefinition>

    init {
        val serializationContext = RedisSerializationContext.newSerializationContext<String, RouteDefinition>()
            .key(RedisSerializer.string())
            .value(JacksonJsonRedisSerializer(RouteDefinition::class.java))
            .hashKey(RedisSerializer.string())
            .hashValue(JacksonJsonRedisSerializer(RouteDefinition::class.java))
            .build()
        reactiveHashOperations = redisTemplate.opsForHash(serializationContext)
        caffeineCache = Caffeine.newBuilder().initialCapacity(128).maximumSize(1024).build()
    }

    /**
     * @return Flux<RouteDefinition>
    </RouteDefinition> */
    override fun getRouteDefinitions(): Flux<RouteDefinition> {
        val routeDefinitions = caffeineCache.asMap().values
        if (routeDefinitions.isEmpty()) {
            return reactiveHashOperations.values(ROUTE_KEY)
                .doOnNext { r -> caffeineCache.put(r.id!!, r) }
        }
        return Flux.fromIterable(routeDefinitions)
            .onErrorContinue { throwable: Throwable, _ ->
                if (log.isErrorEnabled) {
                    log.error(
                        "get routes from redis error cause : {}",
                        throwable.toString(),
                        throwable
                    )
                }
            }
    }

    /**
     * @param route route
     * @return Mono.empty()
     */
    override fun save(route: Mono<RouteDefinition>): Mono<Void> = route.flatMap { r ->
        caffeineCache.put(r.id!!, r)
        reactiveHashOperations.put(ROUTE_KEY, r.id!!, r)
            .flatMap { success: Boolean ->
                if (success) Mono.empty() else defer(
                    String.format(
                        "Could not add route to redis repository: %s",
                        r
                    )
                )
            }
    }

    override fun delete(routeId: Mono<String>): Mono<Void> = routeId.flatMap { id ->
        caffeineCache.invalidate(id)
        reactiveHashOperations.remove(ROUTE_KEY, id)
            .flatMap { success: Long ->
                if (success != 0L) Mono.empty() else defer(
                    String.format(
                        "Could not remove route from redis repository with id: %s",
                        id
                    )
                )
            }
    }

    private fun <T: Any> defer(msg: String): Mono<T> = Mono.defer { Mono.error(NotFoundException(msg)) }

    companion object {
        const val ROUTE_KEY: String = "RouteDefinition"

        private val log = LoggerFactory.getLogger(RedisHashRouteDefinitionRepository::class.java)
    }
}
