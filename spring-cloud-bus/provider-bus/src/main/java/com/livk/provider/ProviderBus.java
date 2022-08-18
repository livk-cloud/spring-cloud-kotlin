package com.livk.provider;

import com.livk.bus.event.LivkBusEvent;
import com.livk.spring.LivkSpring;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.bus.event.Destination;
import org.springframework.cloud.bus.event.PathDestinationFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ProviderBus
 * </p>
 *
 * @author livk
 * @date 2021/11/1
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProviderBus {

    public static void main(String[] args) {
        LivkSpring.run(ProviderBus.class, args);
    }

}

@Slf4j
@RestController
@RequiredArgsConstructor
class BusController {

    private final ApplicationContext applicationContext;

    private final BusProperties busProperties;

    @GetMapping("refresh")
    public void refresh() {
        PathDestinationFactory factory = new PathDestinationFactory();
        Destination destination = factory.getDestination("consumer-bus:6077");
        applicationContext.publishEvent(new LivkBusEvent("livk", busProperties.getId(), destination));
        log.info("event publish!");
    }

}
