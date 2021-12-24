package com.livk.provider.biz;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * <p>
 * ProviderApp
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@EnableCaching
@SpringBootApplication
public class ProviderApp {
    public static void main(String[] args) {
        LivkSpring.runReactive(ProviderApp.class, args);
    }
}
