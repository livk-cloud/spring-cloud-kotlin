package com.livk.consumer.biz.controller;

import com.livk.provider.api.domain.Users;
import com.livk.provider.api.feign.UserRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * UserController
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserRemoteService userRemoteService;

    @GetMapping
    public Flux<Users> users() {
        return userRemoteService.users();
    }

    @PostMapping
    public Mono<Boolean> save(@RequestBody Users users) {
        return userRemoteService.save(users);
    }
}
