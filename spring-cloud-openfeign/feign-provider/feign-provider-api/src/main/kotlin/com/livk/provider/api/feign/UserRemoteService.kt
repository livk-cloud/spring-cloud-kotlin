package com.livk.provider.api.feign

import com.livk.provider.api.domain.Users
import com.livk.provider.api.feign.factory.UserRemoteServiceFallbackFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * @author livk
 */
@FeignClient(
    contextId = "userRemoteService",
    value = "feign-provider-biz",
    fallbackFactory = UserRemoteServiceFallbackFactory::class
)
interface UserRemoteService {
    @Cacheable(value = ["users"], key = "'user:all'", unless = "#result.empty")
    @GetMapping("/users")
    fun users(): List<Users>

    @CacheEvict(value = ["users"], key = "'user:all'")
    @PostMapping("/users")
    fun save(@RequestBody users: Users): Boolean
}
