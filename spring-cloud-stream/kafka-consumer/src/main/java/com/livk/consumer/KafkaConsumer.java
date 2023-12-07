package com.livk.consumer;

import com.livk.commons.SpringLauncher;
import com.livk.stream.entity.StreamMessage;
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
        SpringLauncher.run(args);
    }

    @Bean
    public Consumer<Flux<StreamMessage<String>>> send() {
        return kafkaMessageFlux -> kafkaMessageFlux.subscribe(kafkaMessage -> log.info("[{}]", kafkaMessage));
    }

}
