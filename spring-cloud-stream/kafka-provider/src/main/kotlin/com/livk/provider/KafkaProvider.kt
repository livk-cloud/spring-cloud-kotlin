package com.livk.provider

import com.livk.stream.entity.StreamMessage
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.function.Supplier

/**
 * @author livk
 */
@RestController
@EnableScheduling
@SpringBootApplication
open class KafkaProvider {
    private var buffer: Sinks.Many<StreamMessage<String>> = Sinks.many().multicast().onBackpressureBuffer()


    @PostMapping("send")
    fun send(@RequestBody message: StreamMessage<String>) {
        buffer.tryEmitNext(message)
    }

    @Bean
    open fun send(): Supplier<Flux<StreamMessage<String>>> = Supplier { buffer.asFlux() }
}

fun main(args: Array<String>) {
    runApplication<KafkaProvider>(*args)
}
