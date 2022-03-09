package com.livk.consumer;

import com.livk.common.LivkSpring;
import com.livk.stream.entity.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

/**
 * <p>
 * KafkaConsumer
 * </p>
 *
 * @author livk
 * @date 2022/2/15
 */
@Slf4j
@SpringBootApplication
public class KafkaConsumer {
    public static void main(String[] args) {
        LivkSpring.runServlet(KafkaConsumer.class, args);
    }

    @Bean
    public Consumer<Flux<KafkaMessage<String>>> send() {
        return kafkaMessageFlux -> kafkaMessageFlux.subscribe(kafkaMessage->log.info("[{}]", kafkaMessage));
    }
}
