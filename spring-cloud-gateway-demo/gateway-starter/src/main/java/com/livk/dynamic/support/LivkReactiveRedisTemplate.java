package com.livk.dynamic.support;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * <p>
 * LivkRedisReactiveTemplate
 * </p>
 *
 * @author livk
 * @date 2021/12/28
 */
public class LivkReactiveRedisTemplate extends ReactiveRedisTemplate<String, Object> {
    public LivkReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        this(connectionFactory, new LivkRedisSerializationContext());
    }

    public LivkReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory, RedisSerializationContext<String, Object> serializationContext) {
        super(connectionFactory, serializationContext);
    }

    public LivkReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory, RedisSerializationContext<String, Object> serializationContext, boolean exposeConnection) {
        super(connectionFactory, serializationContext, exposeConnection);
    }
}
