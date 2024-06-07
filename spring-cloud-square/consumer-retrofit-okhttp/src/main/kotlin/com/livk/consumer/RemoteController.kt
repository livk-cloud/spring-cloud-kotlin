package com.livk.consumer

import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author livk
 */
@RestController
class RemoteController(private val remoteService: RemoteService) {
    @GetMapping("/remote/instance")
    fun remoteInstance(): HttpEntity<String> = ResponseEntity.ok(remoteService.instance().execute().body())
}
