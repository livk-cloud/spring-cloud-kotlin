package com.livk.bus;

import com.livk.bus.event.LivkBusEvent;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * LivkBusConfiguration
 * </p>
 *
 * @author livk
 * @date 2021/11/1
 */
@Configuration(proxyBeanMethods = false)
@RemoteApplicationEventScan(basePackageClasses = LivkBusEvent.class)
public class LivkBusConfiguration {

}
