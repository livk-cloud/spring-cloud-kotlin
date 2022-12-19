package com.livk.config;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * <p>
 * ConfigLocalServer
 * </p>
 *
 * @author livk
 * @date 2022/7/27
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigLocalServer {
    public static void main(String[] args) {
        LivkSpring.run(ConfigLocalServer.class, args);
    }
}
