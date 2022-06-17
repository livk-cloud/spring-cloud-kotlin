package com.livk.dynamic.support;

import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;

/**
 * <p>
 * LivkRedisSerializationContext
 * </p>
 *
 * @author livk
 * @date 2022/2/21
 */
public interface RedisSerialization<V> extends RedisSerializationContext<String, V> {

    static <T> Jackson2RedisSerializationContext<T> json(Class<T> targetClass) {
        return new Jackson2RedisSerializationContext<>(targetClass);
    }

    static Jackson2RedisSerializationContext<Object> json() {
        return json(Object.class);
    }

    static JdkRedisSerializationContext java() {
        return new JdkRedisSerializationContext();
    }

    @NonNull
    @Override
    default SerializationPair<String> getKeySerializationPair() {
        return SerializationPair.fromSerializer(RedisSerializer.string());
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    default <HK> SerializationPair<HK> getHashKeySerializationPair() {
        return (SerializationPair<HK>) SerializationPair.fromSerializer(RedisSerializer.string());
    }

    @NonNull
    @Override
    default SerializationPair<String> getStringSerializationPair() {
        return SerializationPair.fromSerializer(RedisSerializer.string());
    }

}
