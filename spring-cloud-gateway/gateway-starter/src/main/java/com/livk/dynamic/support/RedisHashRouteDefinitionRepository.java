package com.livk.dynamic.support;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.livk.autoconfigure.redis.supprot.ReactiveRedisOps;
import com.livk.autoconfigure.redis.util.JacksonSerializerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 使用Redis Hash存储路由信息
 * <p>
 * 也可以参考{@link org.springframework.cloud.gateway.route.RedisRouteDefinitionRepository}使用Redis Value存储
 * </p>
 *
 * @author livk
 * @date 2021/12/28
 */
@Slf4j
public class RedisHashRouteDefinitionRepository implements RouteDefinitionRepository {

    public static final String ROUTE_KEY = "RouteDefinition";

    private final Cache<String, RouteDefinition> caffeineCache;

    private final ReactiveHashOperations<String, String, RouteDefinition> reactiveHashOperations;

    public RedisHashRouteDefinitionRepository(ReactiveRedisOps reactiveRedisOps) {
        RedisSerializationContext<String, RouteDefinition> serializationContext = RedisSerializationContext.<String, RouteDefinition>newSerializationContext()
                .key(RedisSerializer.string())
                .value(JacksonSerializerUtils.json(RouteDefinition.class))
                .hashKey(RedisSerializer.string())
                .hashValue(JacksonSerializerUtils.json(RouteDefinition.class))
                .build();
        reactiveHashOperations = reactiveRedisOps.opsForHash(serializationContext);
        caffeineCache = Caffeine.newBuilder().initialCapacity(128).maximumSize(1024).build();
    }

    /**
     * @return Flux<RouteDefinition>
     */
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        Collection<RouteDefinition> routeDefinitions = caffeineCache.asMap().values();
        if (routeDefinitions.isEmpty()) {
            return reactiveHashOperations.entries(ROUTE_KEY).map(Map.Entry::getValue).doOnNext(r -> caffeineCache.put(r.getId(), r));
        }
        return Flux.fromIterable(routeDefinitions).onErrorContinue((throwable, routeDefinition) -> {
            if (log.isErrorEnabled()) {
                log.error("get routes from redis error cause : {}", throwable.toString(), throwable);
            }
        });
    }

    /**
     * @param route route
     * @return Mono.empty()
     */
    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            caffeineCache.put(r.getId(), r);
            return reactiveHashOperations.put(ROUTE_KEY, r.getId(), r)
                    .flatMap(success -> Boolean.TRUE.equals(success) ? Mono.empty() :
                            defer(String.format("Could not add route to redis repository: %s", r)));
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            caffeineCache.invalidate(id);
            return reactiveHashOperations.remove(ROUTE_KEY, id)
                    .flatMap(success -> success != 0 ? Mono.empty() :
                            defer(String.format("Could not remove route from redis repository with id: %s", id)));
        });
    }

    private <T> Mono<T> defer(String msg) {
        return Mono.defer(() -> Mono.error(new NotFoundException(msg)));
    }
}
