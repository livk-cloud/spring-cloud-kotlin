package com.livk.provider.biz;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p>
 * ProviderApp
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class ProviderApp {

    public static void main(String[] args) {
        SpringLauncher.run(ProviderApp.class, args);
    }

}
