package com.livk.provider.api.feign.factory

import com.livk.provider.api.domain.Users
import com.livk.provider.api.feign.UserRemoteService
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.stereotype.Component

/**
 * @author livk
 */
@Component
class UserRemoteServiceFallbackFactory : FallbackFactory<UserRemoteService> {
    override fun create(cause: Throwable): UserRemoteService = object : UserRemoteService {
        override fun users(): List<Users> = emptyList()

        override fun save(users: Users): Boolean = false
    }
}
