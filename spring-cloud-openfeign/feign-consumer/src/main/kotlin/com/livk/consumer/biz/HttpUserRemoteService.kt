package com.livk.consumer.biz

import com.livk.context.http.annotation.HttpProvider
import com.livk.provider.api.domain.Users
import org.springframework.web.service.annotation.GetExchange

/**
 * @author livk
 */
@HttpProvider(value = "httpUserRemoteService", url = "http://feign-provider-biz")
interface HttpUserRemoteService {
    @GetExchange("/users")
    fun users(): List<Users>
}
