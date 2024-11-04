package com.livk.consumer

import com.livk.stream.entity.StreamMessage
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Flux

/**
 * @author livk
 */
@SpringBootApplication
open class RabbitConsumer {

    private val log = LoggerFactory.getLogger(RabbitConsumer::class.java)

    @Bean
    open fun send(): (Flux<StreamMessage<String>>) -> Unit = { streamMessageFlux ->
        streamMessageFlux.subscribe { streamMessage ->
            log.info(
                "[{}]",
                streamMessage
            )
        }
    }
}

fun main(args: Array<String>) {
    runApplication<RabbitConsumer>(*args)
}
