package com.livk.provider.api.feign

import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import tools.jackson.databind.JsonNode

/**
 * @author livk
 */
@HttpExchange
interface HttpUserRemoteService {
    @GetExchange("/users")
    fun users(): List<JsonNode>
}
