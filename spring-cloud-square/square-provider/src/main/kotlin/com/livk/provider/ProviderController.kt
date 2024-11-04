package com.livk.provider

import com.livk.commons.SpringContextHolder
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author livk
 */
@RestController
class ProviderController(private val discoveryClient: DiscoveryClient) {
    @GetMapping("instance")
    fun instance(): HttpEntity<String> = ResponseEntity.ok(
        discoveryClient
            .getInstances(
                SpringContextHolder.getProperty("spring.application.name")
            )
            .stream().findFirst().map { it.instanceId }.orElse("is null")
    )
}
