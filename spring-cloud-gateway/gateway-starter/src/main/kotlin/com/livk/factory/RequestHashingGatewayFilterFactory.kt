package com.livk.factory

import com.livk.auto.service.annotation.SpringAutoService
import org.bouncycastle.util.encoders.Hex
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.http.codec.HttpMessageReader
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/**
 *
 *
 * This filter hashes the request body, placing the value in the X-Hash header.
 * Note: This causes the gateway to be memory constrained.
 * Sample usage: RequestHashing=SHA-256
 *
 *
 * @author livk
 */
@Component
@SpringAutoService
class RequestHashingGatewayFilterFactory : AbstractGatewayFilterFactory<RequestHashingGatewayFilterFactory.Config>(
    Config::class.java
) {
    private val messageReaders: List<HttpMessageReader<*>> = HandlerStrategies.withDefaults().messageReaders()

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            ServerWebExchangeUtils
                .cacheRequestBodyAndRequest(exchange) { httpRequest ->
                    ServerRequest
                        .create(
                            exchange.mutate()
                                .request(httpRequest)
                                .build(), messageReaders
                        )
                        .bodyToMono(String::class.java)
                        .doOnNext { requestPayload: String ->
                            exchange
                                .attributes[HASH_ATTR] = computeHash(config.messageDigest, requestPayload)
                        }
                        .then(Mono.defer {
                            var cachedRequest = exchange.getAttribute<ServerHttpRequest>(
                                ServerWebExchangeUtils.CACHED_SERVER_HTTP_REQUEST_DECORATOR_ATTR
                            )
                            Assert.notNull(
                                cachedRequest,
                                "cache request shouldn't be null"
                            )
                            exchange.attributes
                                .remove(ServerWebExchangeUtils.CACHED_SERVER_HTTP_REQUEST_DECORATOR_ATTR)
                            val hash = exchange.getAttribute<String>(HASH_ATTR)
                            cachedRequest = cachedRequest!!.mutate()
                                .header(HASH_HEADER, hash)
                                .build()
                            chain.filter(
                                exchange.mutate()
                                    .request(cachedRequest)
                                    .build()
                            )
                        })
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
            private set

        fun algorithm(algorithm: String) {
            messageDigest = MessageDigest.getInstance(algorithm)
        }
    }

    companion object {
        private const val HASH_ATTR = "hash"

        private const val HASH_HEADER = "X-Hash"
    }
}
