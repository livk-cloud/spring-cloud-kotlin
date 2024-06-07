package com.livk.bus.event

import org.springframework.cloud.bus.event.Destination
import org.springframework.cloud.bus.event.RemoteApplicationEvent

/**
 * @author livk
 */
class LivkBusEvent(source: Any, originService: String, destination: Destination) :
    RemoteApplicationEvent(source, originService, destination)
