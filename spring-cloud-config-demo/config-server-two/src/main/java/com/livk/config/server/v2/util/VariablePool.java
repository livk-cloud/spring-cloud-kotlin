package com.livk.config.server.v2.util;

import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * <p>
 * VariablePool
 * </p>
 *
 * @author livk
 * @date 2022/1/30
 */
public class VariablePool {
    public static final Map<String, Map<Class<?>, String>> pool = new HashMap<>();

    private static final String regex = "^(\\$\\{)(.)+(\\})$";
    private static final Pattern pattern;

    static {
        pattern = Pattern.compile(regex);
    }

    public static void add(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Value.class)) {
                Value annotation = field.getAnnotation(Value.class);
                String annoValue = annotation.value();
                if (!pattern.matcher(annoValue).matches())
                    continue;

                annoValue = annoValue.replace("${", "");
                annoValue = annoValue.substring(0, annoValue.length() - 1);

                Map<Class<?>, String> clazzMap = Optional.ofNullable(pool.get(annoValue))
                        .orElse(new HashMap<>());
                clazzMap.put(clazz, field.getName());
                pool.put(annoValue, clazzMap);
            }
        }
    }

    public static Map<String, Map<Class<?>, String>> getPool() {
        return pool;
    }
}
