package com.livk.consumer.biz;

import com.livk.common.LivkSpring;
import com.livk.provider.api.feign.UserRemoteService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>
 * ConsumerApp
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@EnableFeignClients(basePackageClasses = UserRemoteService.class)
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class ConsumerApp {

	public static void main(String[] args) {
		LivkSpring.run(ConsumerApp.class, args);
	}

}
