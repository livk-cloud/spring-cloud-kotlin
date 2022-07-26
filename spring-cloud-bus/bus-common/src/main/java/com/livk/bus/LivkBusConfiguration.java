package com.livk.bus;

import com.livk.bus.event.LivkBusEvent;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;

/**
 * <p>
 * LivkBusConfiguration
 * </p>
 *
 * @author livk
 * @date 2021/11/1
 */
@AutoConfiguration
@RemoteApplicationEventScan(basePackageClasses = LivkBusEvent.class)
public class LivkBusConfiguration {

}
