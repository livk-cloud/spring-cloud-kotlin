package com.livk.provider

import com.livk.stream.entity.StreamMessage
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * @author livk
 */
@Component
class SendMessage(private val streamBridge: StreamBridge) {
    @Scheduled(cron = "0/5 * * * * ?")
    fun send() {
        val message = StreamMessage(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString() + "hello",
            UUID.randomUUID().toString() + "|||" + UUID.randomUUID()
        )
        streamBridge.send(KAFKA_TOPIC, message)
    }

    companion object {
        const val KAFKA_TOPIC: String = "livk-topic"
    }
}
