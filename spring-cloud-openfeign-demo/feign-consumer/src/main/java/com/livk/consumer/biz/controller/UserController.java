package com.livk.consumer.biz.controller;

import com.livk.provider.api.domain.Users;
import com.livk.provider.api.feign.UserRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Users> users() {
        return userRemoteService.users();
    }

    @PostMapping
    public Boolean save(@RequestBody Users users) {
        return userRemoteService.save(users);
    }
}
