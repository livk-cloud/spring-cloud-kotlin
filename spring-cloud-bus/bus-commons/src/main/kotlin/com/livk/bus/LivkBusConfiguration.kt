package com.livk.bus

import com.livk.auto.service.annotation.SpringAutoService
import com.livk.bus.event.LivkBusEvent
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan

/**
 *
 * @author livk
 */
@AutoConfiguration
@SpringAutoService
@RemoteApplicationEventScan(basePackageClasses = [LivkBusEvent::class])
open class LivkBusConfiguration
