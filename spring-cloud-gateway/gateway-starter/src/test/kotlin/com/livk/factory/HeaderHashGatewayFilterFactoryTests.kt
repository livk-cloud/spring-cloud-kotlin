package com.livk.factory

import org.junit.jupiter.api.Assertions.*
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.security.MessageDigest
import kotlin.test.Test

/**
 * <p>
 * HeaderHashGatewayFilterFactoryTests
 * </p>
 *
 * @author livk
 * @date 2025/12/6
 */
class HeaderHashGatewayFilterFactoryTests {
    @Test
    fun `should allow request when header valid`() {
        val body = "hello world"
        val md = MessageDigest.getInstance("SHA-512")
        val expectedHash = hex(md.digest(body.toByteArray()))

        val request = MockServerHttpRequest.post("/post")
            .header("X-Hash", expectedHash)
            .body(body)

        val exchange = MockServerWebExchange.from(request)

        val factory = HeaderHashGatewayFilterFactory()
        val config = HeaderHashGatewayFilterFactory.Config()
        config.algorithm("SHA-512")
        val filter = factory.apply(config)

        var chainInvoked = false
        val chain = GatewayFilterChain { _: ServerWebExchange ->
            chainInvoked = true
            Mono.empty()
        }

        filter.filter(exchange, chain).block()

        assertTrue(chainInvoked) // 走了下游
    }

    private fun hex(bytes: ByteArray) = bytes.joinToString("") { "%02x".format(it) }
}
