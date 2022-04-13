package com.livk.consumer;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * OkhttpConsumerApp
 * </p>
 *
 * @author livk
 * @date 2022/4/13
 */
@SpringBootApplication
public class OkhttpConsumerApp {
    public static void main(String[] args) {
        LivkSpring.run(OkhttpConsumerApp.class, args);
    }
}
