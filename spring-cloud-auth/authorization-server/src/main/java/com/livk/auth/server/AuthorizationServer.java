package com.livk.auth.server;


import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * AuthorizationServer
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 */
@SpringBootApplication
public class AuthorizationServer {
    public static void main(String[] args) {
        LivkSpring.run(AuthorizationServer.class, args);
    }
}
