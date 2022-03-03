package com.livk.config.server.v2;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ConfigServerTwo
 * </p>
 *
 * @author livk
 * @date 2022/1/30
 */
@SpringBootApplication
public class ConfigServerTwo {
    public static void main(String[] args) {
        LivkSpring.runServlet(ConfigServerTwo.class, args);
    }
}
