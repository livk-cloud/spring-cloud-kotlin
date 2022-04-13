package com.livk.client;

import com.livk.common.LivkSpring;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ConfigClient
 * </p>
 *
 * @author livk
 */
@RestController
@SpringBootApplication
public class ConfigClient {

	public static void main(String[] args) {
		LivkSpring.run(ConfigClient.class, args);
	}

	@Value("${foo}")
	String foo;

	@RequestMapping(value = "/foo")
	public String hi() {
		return foo;
	}

}
