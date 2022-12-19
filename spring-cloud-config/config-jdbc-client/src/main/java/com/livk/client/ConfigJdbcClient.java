package com.livk.client;

import com.livk.commons.spring.LivkSpring;
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
public class ConfigJdbcClient {

    @Value("${foo}")
    String foo;

    public static void main(String[] args) {
        LivkSpring.run(ConfigJdbcClient.class, args);
    }

    @RequestMapping(value = "/foo")
    public String hi() {
        return foo;
    }

}
