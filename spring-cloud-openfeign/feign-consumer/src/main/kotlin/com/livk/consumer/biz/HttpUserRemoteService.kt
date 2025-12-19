package com.livk.consumer.biz

import com.livk.provider.api.domain.Users
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

/**
 * @author livk
 */
@HttpExchange(value = "httpUserRemoteService", url = "http://feign-provider-biz")
interface HttpUserRemoteService {
    @GetExchange("/users")
    fun users(): List<Users>
}
