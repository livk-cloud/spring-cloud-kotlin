package com.livk.provider;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * KafkaProvider
 * </p>
 *
 * @author livk
 * @date 2022/2/15
 */
@EnableScheduling
@SpringBootApplication
public class KafkaProvider {
    public static void main(String[] args) {
        LivkSpring.runServlet(KafkaProvider.class, args);
    }
}
