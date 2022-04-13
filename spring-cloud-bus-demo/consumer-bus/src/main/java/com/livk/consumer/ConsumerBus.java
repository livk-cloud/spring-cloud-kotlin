package com.livk.consumer;

import com.livk.bus.listener.EnableRemoteEventListener;
import com.livk.common.LivkSpring;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p>
 * ConsumerBus
 * </p>
 *
 * @author livk
 * @date 2021/11/1
 */
@Slf4j
@EnableDiscoveryClient
@EnableRemoteEventListener
@SpringBootApplication
public class ConsumerBus {

	public static void main(String[] args) {
		LivkSpring.run(ConsumerBus.class, args);
	}

}
