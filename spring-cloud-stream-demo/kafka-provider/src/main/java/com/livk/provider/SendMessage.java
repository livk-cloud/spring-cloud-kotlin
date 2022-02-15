package com.livk.provider;

import com.livk.stream.entity.KafkaMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * <p>
 * SendMessage
 * </p>
 *
 * @author livk
 * @date 2022/2/15
 */
@Component
@RequiredArgsConstructor
public class SendMessage {

    public static final String KAFKA_TOPIC = "livk-topic";

    private final StreamBridge streamBridge;

    @Scheduled(cron = "0/5 * * * * ?")
    public void send() {
        KafkaMessage<String> message = new KafkaMessage<>();
        message.setId(UUID.randomUUID().toString());
        message.setMsg(UUID.randomUUID() + "hello");
        message.setData(UUID.randomUUID() + "|||" + UUID.randomUUID());
        streamBridge.send(KAFKA_TOPIC, message);
    }
}
