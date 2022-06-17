```yaml
spring:
  cloud:
    stream:
      bindings:
        send-in-0:
          content-type: application/json
```

```java
public class KafkaConsumer {
    @Bean
    public Consumer<Flux<KafkaMessage<String>>> send() {
        return kafkaMessageFlux -> kafkaMessageFlux.subscribe(kafkaMessage->log.info("[{}]", kafkaMessage));
    }
}
```

> 当consumer泛型为其他类型,使用json处理
