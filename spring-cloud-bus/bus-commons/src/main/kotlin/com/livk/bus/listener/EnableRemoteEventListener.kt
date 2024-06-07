package com.livk.bus.listener

import org.springframework.context.annotation.Import

/**
 * @author livk
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Import(
    LivkEventListener::class
)
annotation class EnableRemoteEventListener
