package com.livk.filter

import com.livk.auto.service.annotation.SpringAutoService
import com.livk.commons.io.DataBufferUtils
import com.livk.commons.jackson.JsonMapperUtils
import org.reactivestreams.Publisher
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.LimitedDataBufferList
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tools.jackson.databind.node.JsonNodeFactory
import tools.jackson.databind.node.ObjectNode
import java.nio.charset.StandardCharsets

/**
 * @author livk
 */
@Component
@SpringAutoService
class ResultHandlerWebFilter : GlobalFilter, Ordered {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val originalResponse = exchange.response
        val bufferFactory = originalResponse.bufferFactory()
        val decoratedResponse: ServerHttpResponse = object : ServerHttpResponseDecorator(originalResponse) {
            override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
                val dataBufferFlux = Flux.from(body).collect(
                    { LimitedDataBufferList(-1) }) { obj: LimitedDataBufferList, buffer ->
                    obj.add(
                        buffer
                    )
                }
                    .filter { list: LimitedDataBufferList -> !list.isEmpty() }
                    .map { list: LimitedDataBufferList -> list[0].factory().join(list) }
                    .doOnDiscard(DataBuffer::class.java, DataBufferUtils::release)
                    .map { dataBuffer: DataBuffer ->
                        val content = ByteArray(dataBuffer.readableByteCount())
                        dataBuffer.read(content)
                        DataBufferUtils.release(dataBuffer)
                        val result = String(content, StandardCharsets.UTF_8)

                        val bytes = resultHandler(result)
                        headers.contentLength = bytes.size.toLong()
                        headers.contentType = MediaType.APPLICATION_JSON
                        bufferFactory.wrap(bytes)
                    }
                return super.writeWith(dataBufferFlux)
            }
        }
        return chain.filter(exchange.mutate().response(decoratedResponse).build())
    }

    protected fun resultHandler(result: String): ByteArray {
        val jsonNode = JsonMapperUtils.readTree(result)
        val node = ObjectNode(JsonNodeFactory.instance)
        node.put("code", 200)
            .put("msg", "ok")
            .set("data", jsonNode)
        return JsonMapperUtils.writeValueAsBytes(node)
    }

    override fun getOrder(): Int {
        return Ordered.HIGHEST_PRECEDENCE
    }
}
