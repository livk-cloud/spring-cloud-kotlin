package com.livk.provider.biz.controller

import com.livk.provider.api.domain.Users
import com.livk.provider.biz.mapper.UserMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author livk
 */
@RestController
@RequestMapping("users")
open class UsersController(private val userMapper: UserMapper) {
    @Cacheable(value = ["users"], key = "'user:all'")
    @GetMapping
    open fun users(): List<Users> {
        log.info("provider")
        return userMapper.selectList()
    }

    @PostMapping
    fun save(@RequestBody users: Users): Boolean = userMapper.insert(users) != 0

    @CacheEvict(value = ["users"], key = "'user:all'")
    @DeleteMapping("{id}")
    open fun delete(@PathVariable("id") id: Long): Boolean = userMapper.deleteById(id) != 0

    companion object {
        private val log: Logger = LoggerFactory.getLogger(UsersController::class.java)
    }
}
