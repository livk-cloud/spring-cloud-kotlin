package com.livk.provider;

import com.livk.bus.event.LivkBusEvent;
import com.livk.common.LivkSpring;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.BusProperties;
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
public class ProviderBus {

    public static void main(String[] args) {
        LivkSpring.runReactive(ProviderBus.class, args);
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
        applicationContext.publishEvent(new LivkBusEvent("livk", busProperties.getId(), () -> "consumer-bus:6077:**"));
        log.info("event publish!");
    }

}
