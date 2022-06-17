package com.livk.provider;

import com.livk.common.LivkSpring;
import com.livk.stream.entity.KafkaMessage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

/**
 * <p>
 * KafkaProvider
 * </p>
 *
 * @author livk
 * @date 2022/2/15
 */
@RestController
@EnableScheduling
@SpringBootApplication
public class KafkaProvider {

    Sinks.Many<KafkaMessage<String>> buffer = Sinks.many().multicast().onBackpressureBuffer();

    public static void main(String[] args) {
        LivkSpring.run(KafkaProvider.class, args);
    }

    @PostMapping("send")
    public void send(@RequestBody KafkaMessage<String> message) {
        buffer.tryEmitNext(message);
    }

    @Bean
    public Supplier<Flux<KafkaMessage<String>>> send() {
        return buffer::asFlux;
    }

}
