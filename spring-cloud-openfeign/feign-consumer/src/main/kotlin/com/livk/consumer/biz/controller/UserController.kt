package com.livk.consumer.biz.controller

import com.livk.provider.api.domain.Users
import com.livk.provider.api.feign.HttpUserRemoteService
import com.livk.provider.api.feign.UserRemoteService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

/**
 * @author livk
 */
@RestController
@RequestMapping("user")
class UserController(
    private val userRemoteService: UserRemoteService,
    private val httpUserRemoteService: HttpUserRemoteService
) {
    @GetMapping
    fun users(): List<Users> {
        log.info("consumer")
        log.info("{}", httpUserRemoteService.users())
        return userRemoteService.users()
    }

    @PostMapping
    fun save(@RequestBody users: Users): Boolean {
        return userRemoteService.save(users)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(UserController::class.java)
    }
}
