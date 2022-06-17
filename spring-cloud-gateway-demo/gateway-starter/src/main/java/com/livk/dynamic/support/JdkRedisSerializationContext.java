package com.livk.dynamic.support;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;

/**
 * <p>
 * JdkRedisSerializationContext
 * </p>
 *
 * @author livk
 * @date 2022/2/21
 */
public class JdkRedisSerializationContext implements RedisSerialization<Object> {

    @NonNull
    @Override
    public SerializationPair<Object> getValueSerializationPair() {
        return SerializationPair.fromSerializer(RedisSerializer.java());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <HV> SerializationPair<HV> getHashValueSerializationPair() {
        return (SerializationPair<HV>) SerializationPair.fromSerializer(RedisSerializer.java());
    }

}
