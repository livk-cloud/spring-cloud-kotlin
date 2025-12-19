package com.livk.factory

import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.security.MessageDigest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * @author livk
 */
class RequestHashingGatewayFilterFactoryTests {

    @Test
    fun `should add header`() {
        // given request
        val body = "hello world"
        val request = MockServerHttpRequest.post("/post")
            .body(body)
        val exchange = MockServerWebExchange.from(request)

        val factory = RequestHashingGatewayFilterFactory()
        val config = RequestHashingGatewayFilterFactory.Config()
        config.algorithm("SHA-512")
        val filter = factory.apply(config)

        // fake filter chain: just capture mutated request
        var mutatedRequest: ServerHttpRequest? = null
        val chain = GatewayFilterChain { ex: ServerWebExchange ->
            mutatedRequest = ex.request
            Mono.empty()
        }

        // when filter applies
        filter.filter(exchange, chain).block()

        // then verify hash header
        val expectedHash = hex(MessageDigest.getInstance("SHA-512").digest(body.toByteArray()))
        val headerValue = mutatedRequest!!.headers.getFirst("X-Hash")

        assertEquals(expectedHash, headerValue)
    }

    private fun hex(bytes: ByteArray) = bytes.joinToString("") { "%02x".format(it) }
}
