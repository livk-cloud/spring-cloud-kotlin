package com.livk.provider

import com.livk.bus.event.LivkBusEvent
import org.slf4j.LoggerFactory
import org.springframework.cloud.bus.BusProperties
import org.springframework.cloud.bus.event.PathDestinationFactory
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * <p>
 * BusController
 * </p>
 *
 * @author livk
 * @date 2024/6/6
 */
@RestController
class BusController(private val applicationContext: ApplicationContext, private val busProperties: BusProperties) {

    private val log = LoggerFactory.getLogger(BusController::class.java)

    @GetMapping("refresh")
    fun refresh() {
        val factory = PathDestinationFactory()
        val destination = factory.getDestination("consumer-bus:6077")
        applicationContext.publishEvent(LivkBusEvent("livk", busProperties.id, destination))
        log.info("event publish!")
    }
}
