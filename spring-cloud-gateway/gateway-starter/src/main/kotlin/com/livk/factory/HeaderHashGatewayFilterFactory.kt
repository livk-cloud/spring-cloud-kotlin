package com.livk.factory

import com.livk.auto.service.annotation.SpringAutoService
import com.livk.commons.jackson.util.JsonMapperUtils
import org.bouncycastle.util.encoders.Hex
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.HttpMessageReader
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/**
 * @author livk
 */
@Component
@SpringAutoService
class HeaderHashGatewayFilterFactory : AbstractGatewayFilterFactory<HeaderHashGatewayFilterFactory.Config>(
    Config::class.java
) {
    private val messageReaders: List<HttpMessageReader<*>> = HandlerStrategies.withDefaults().messageReaders()

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            ServerWebExchangeUtils
                .cacheRequestBodyAndRequest(
                    exchange
                ) { httpRequest ->
                    ServerRequest
                        .create(
                            exchange.mutate()
                                .request(httpRequest)
                                .build(), messageReaders
                        )
                        .bodyToMono(String::class.java)
                        .log()
                        .flatMap { body: String ->
                            val headerValueList = exchange.request.headers[HEADER_NAME]
                            if (!CollectionUtils.isEmpty(headerValueList) &&
                                headerValueList!!.contains(computeHash(config.messageDigest, body))
                            ) {
                                return@flatMap chain.filter(exchange)
                            }
                            val response = exchange.response
                            response.headers.contentType = MediaType.APPLICATION_JSON
                            response.setStatusCode(HttpStatus.OK)
                            val result = mapOf("code" to "403", "msg" to "缺少Header:X-Hash")
                            response.writeWith(
                                Mono.just(
                                    response.bufferFactory().wrap(
                                        JsonMapperUtils.writeValueAsString(result).toByteArray(
                                            StandardCharsets.UTF_8
                                        )
                                    )
                                )
                            )
                        }
                }
        }
    }

    override fun shortcutFieldOrder(): List<String> {
        return listOf("algorithm")
    }

    private fun computeHash(messageDigest: MessageDigest, requestPayload: String): String {
        return Hex.toHexString(messageDigest.digest(requestPayload.toByteArray(StandardCharsets.UTF_8)))
    }

    class Config {
        lateinit var messageDigest: MessageDigest

        fun algorithm(algorithm: String) {
            messageDigest = MessageDigest.getInstance(algorithm)
        }
    }

    companion object {
        private const val HEADER_NAME = "X-Hash"
    }
}
