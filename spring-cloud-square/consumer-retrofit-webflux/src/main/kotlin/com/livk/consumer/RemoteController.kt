package com.livk.consumer

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * @author livk
 */
@RestController
class RemoteController(private val remoteService: RemoteService) {
    @GetMapping("/remote/instance")
    fun remoteInstance(): Mono<String> = remoteService.instance()
}
