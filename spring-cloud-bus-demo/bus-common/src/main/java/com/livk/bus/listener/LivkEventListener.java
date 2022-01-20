package com.livk.bus.listener;

import com.livk.bus.event.LivkBusEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.Nullable;

/**
 * <p>
 * LivkEventListener
 * </p>
 *
 * @author livk
 * @date 2021/11/1
 */
@Slf4j
public class LivkEventListener implements ApplicationListener<LivkBusEvent> {

	@Override
	public void onApplicationEvent(@Nullable LivkBusEvent event) {
		log.info("listener:{}", event);
	}

}
