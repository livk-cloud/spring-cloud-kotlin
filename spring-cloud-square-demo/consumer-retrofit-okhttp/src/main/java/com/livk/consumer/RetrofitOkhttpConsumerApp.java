package com.livk.consumer;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RetrofitOkhttpConsumerApp
 * </p>
 *
 * @author livk
 * @date 2022/4/13
 */
@SpringBootApplication
public class RetrofitOkhttpConsumerApp {

	public static void main(String[] args) {
		LivkSpring.run(RetrofitOkhttpConsumerApp.class, args);
	}

}
