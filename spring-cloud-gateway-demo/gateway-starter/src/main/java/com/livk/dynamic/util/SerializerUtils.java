package com.livk.dynamic.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * <p>
 * JacksonRedisUtils
 * </p>
 *
 * @author livk
 * @date 2022/1/19
 */
@UtilityClass
public class SerializerUtils {

    public <T> RedisSerializer<T> getJacksonSerializer(ObjectMapper mapper, Class<T> targetClass) {
        var serializer = new Jackson2JsonRedisSerializer<>(targetClass);
        mapper.registerModule(new JavaTimeModule());
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        serializer.setObjectMapper(mapper);
        return serializer;
    }

    public <T> RedisSerializer<T> getJacksonSerializer(Class<T> targetClass) {
        return getJacksonSerializer(JsonMapper.builder().build(), targetClass);
    }

}
