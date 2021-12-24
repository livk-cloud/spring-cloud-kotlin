package com.livk.provider.api.feign;

import com.livk.provider.api.domain.Users;
import com.livk.provider.api.feign.factory.UserRemoteServiceFallbackFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * UserRemoteService
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@FeignClient(contextId = "userRemoteService", value = "feign-provider-biz",
        fallbackFactory = UserRemoteServiceFallbackFactory.class)
public interface UserRemoteService {

    @Cacheable(value = "users", key = "'user:all'", unless = "#result.empty")
    @GetMapping("/users")
    List<Users> users();

    @CacheEvict(value = "users", key = "'user:all'")
    @PostMapping("/users")
    Boolean save(@RequestBody Users users);
}
