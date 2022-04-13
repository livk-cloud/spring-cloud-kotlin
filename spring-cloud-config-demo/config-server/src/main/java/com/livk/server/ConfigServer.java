package com.livk.server;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * <p>
 * ConfigServer
 * </p>
 *
 * @author livk
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServer {

	public static void main(String[] args) {
		LivkSpring.run(ConfigServer.class, args);
	}

}
