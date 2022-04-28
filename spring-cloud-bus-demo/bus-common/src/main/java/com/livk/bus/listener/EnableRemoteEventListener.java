package com.livk.bus.listener;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 * EnableRemoteEventListener
 * </p>
 *
 * @author livk
 * @date 2022/1/7
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LivkEventListener.class)
public @interface EnableRemoteEventListener {

}
