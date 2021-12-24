package com.livk.bus.event;

import org.springframework.cloud.bus.event.Destination;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * <p>
 * LivkBusEvent
 * </p>
 *
 * @author livk
 * @date 2021/11/1
 */
public class LivkBusEvent extends RemoteApplicationEvent {

	public LivkBusEvent() {
		super();
	}

	public LivkBusEvent(Object source, String originService, Destination destination) {

		super(source, originService, destination);
	}

	@Deprecated
	public LivkBusEvent(Object source, String originService, String destinationService) {

		super(source, originService, destinationService);
	}

}
