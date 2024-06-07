package com.livk.bus.listener

import com.livk.bus.event.LivkBusEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener

/**
 * @author livk
 */
class LivkEventListener : ApplicationListener<LivkBusEvent> {
    private val log: Logger = LoggerFactory.getLogger(LivkEventListener::class.java)

    override fun onApplicationEvent(event: LivkBusEvent) {
        log.info("listener:{}", event)
    }
}
