package com.livk;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

/**
 * <p>
 * FunctionApp
 * </p>
 *
 * @author livk
 * @date 2021/12/3
 */
@SpringBootApplication
public class FunctionApp {
    public static void main(String[] args) {
        LivkSpring.run(FunctionApp.class, args);
    }

    @Bean
    public Function<String, Object> livk() {
        return value -> new StringBuilder(value).reverse().toString();
    }
}
