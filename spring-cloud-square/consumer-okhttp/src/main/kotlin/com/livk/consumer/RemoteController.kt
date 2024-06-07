package com.livk.consumer

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author livk
 */
@RestController
class RemoteController(private val builder: OkHttpClient.Builder) {

    // 本质就是使用okhttp调用，和restTemplate没什么区别
    @GetMapping("/remote/instance")
    fun remoteInstance(): HttpEntity<String> {
        val request: Request = Request.Builder().url("http://square-provider/instance").build()
        return builder.build().newCall(request).execute().use { response ->
            ResponseEntity.ok(response.body!!.string())
        }
    }
}
