package com.livk.dynamic.support;

import com.livk.dynamic.util.SerializerUtils;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;

/**
 * <p>
 * LivkRedisSerializationContext
 * </p>
 *
 * @author livk
 * @date 2021/12/5
 */
public class Jackson2RedisSerializationContext<T> implements RedisSerialization<T> {

	private final RedisSerializer<T> serializer;

	public Jackson2RedisSerializationContext(Class<T> targetClass) {
		this.serializer = SerializerUtils.getJacksonSerializer(targetClass);
	}

	@NonNull
	@Override
	public RedisSerializationContext.SerializationPair<T> getValueSerializationPair() {
		return RedisSerializationContext.SerializationPair.fromSerializer(serializer);
	}

	@SuppressWarnings("unchecked")
	@NonNull
	@Override
	public <HV> RedisSerializationContext.SerializationPair<HV> getHashValueSerializationPair() {
		return (RedisSerializationContext.SerializationPair<HV>) RedisSerializationContext.SerializationPair
				.fromSerializer(serializer);
	}

}
