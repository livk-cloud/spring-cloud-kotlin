package com.livk.config;

import com.livk.commons.spring.LivkSpring;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ConfigLocalClient
 * </p>
 *
 * @author livk
 * @date 2022/7/27
 */
@RestController
@SpringBootApplication
public class ConfigLocalClient {

    @Value("${app.name}")
    String name;

    public static void main(String[] args) {
        LivkSpring.run(ConfigLocalClient.class, args);
    }

    @GetMapping
    public HttpEntity<String> get() {
        return ResponseEntity.ok(name);
    }
}
