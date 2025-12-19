package com.livk.filter

import com.livk.commons.io.DataBufferUtils
import com.livk.commons.jackson.JsonMapperUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.MediaType
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.http.server.reactive.MockServerHttpResponse
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import kotlin.test.Test

/**
 * <p>
 * ResultHandlerWebFilterTests
 * </p>
 *
 * @author livk
 * @date 2025/12/6
 */
class ResultHandlerWebFilterTests {

    private val filter = ResultHandlerWebFilter()

    @Test
    fun `should wrap json response into code_msg_data`() {

        // 模拟请求
        val request = MockServerHttpRequest.get("/test").build()
        val exchange: ServerWebExchange = MockServerWebExchange.from(request)

        // 设置响应 content-type 为 JSON
        exchange.response.headers.contentType = MediaType.APPLICATION_JSON

        // 模拟 chain：写入原始 JSON
        val chain = GatewayFilterChain { ex ->
            val bytes = """{ "name": "livk" }""".toByteArray()
            ex.response.writeWith(Mono.just(ex.response.bufferFactory().wrap(bytes)))
        }

        // 执行 filter
        filter.filter(exchange, chain).block()

        val response = exchange.response as MockServerHttpResponse
        val body = DataBufferUtils.transformByte(response.body).block()
        val node = JsonMapperUtils.readTree(body)

        assertTrue(node.has("code"))
        assertEquals(200, node.get("code").asInt())
        assertTrue(node.has("msg"))
        assertEquals("ok", node.get("msg").asString())
        assertTrue(node.has("data"))
        assertTrue(node.get("data").has("name"))
        assertEquals("livk", node.get("data").get("name").asString())
    }
}
