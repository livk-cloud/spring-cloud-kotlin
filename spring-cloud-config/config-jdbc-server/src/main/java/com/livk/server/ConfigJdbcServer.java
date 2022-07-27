package com.livk.server;


import com.livk.spring.LivkSpring;
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
public class ConfigJdbcServer {

    public static void main(String[] args) {
        LivkSpring.run(ConfigJdbcServer.class, args);
    }

}
