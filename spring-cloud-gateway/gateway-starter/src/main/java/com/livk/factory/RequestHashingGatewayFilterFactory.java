package com.livk.factory;

import com.livk.auto.service.annotation.SpringAutoService;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CACHED_SERVER_HTTP_REQUEST_DECORATOR_ATTR;

/**
 * <p>
 * This filter hashes the request body, placing the value in the X-Hash header.
 * Note: This causes the gateway to be memory constrained.
 * Sample usage: RequestHashing=SHA-256
 * </p>
 *
 * @author livk
 * @date 2022/8/29
 */
@Component
@SpringAutoService
public class RequestHashingGatewayFilterFactory extends
        AbstractGatewayFilterFactory<RequestHashingGatewayFilterFactory.Config> {

    private static final String HASH_ATTR = "hash";

    private static final String HASH_HEADER = "X-Hash";

    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    public RequestHashingGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> ServerWebExchangeUtils
                .cacheRequestBodyAndRequest(exchange, (httpRequest) -> ServerRequest
                        .create(exchange.mutate()
                                .request(httpRequest)
                                .build(), messageReaders)
                        .bodyToMono(String.class)
                        .doOnNext(requestPayload -> exchange
                                .getAttributes()
                                .put(HASH_ATTR, computeHash(config.getMessageDigest(), requestPayload)))
                        .then(Mono.defer(() -> {
                            ServerHttpRequest cachedRequest = exchange.getAttribute(
                                    CACHED_SERVER_HTTP_REQUEST_DECORATOR_ATTR);
                            Assert.notNull(cachedRequest,
                                    "cache request shouldn't be null");
                            exchange.getAttributes()
                                    .remove(CACHED_SERVER_HTTP_REQUEST_DECORATOR_ATTR);
                            String hash = exchange.getAttribute(HASH_ATTR);
                            cachedRequest = cachedRequest.mutate()
                                    .header(HASH_HEADER, hash)
                                    .build();
                            return chain.filter(exchange.mutate()
                                    .request(cachedRequest)
                                    .build());
                        })));
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("algorithm");
    }

    private String computeHash(MessageDigest messageDigest, String requestPayload) {
        return Hex.toHexString(messageDigest.digest(requestPayload.getBytes(StandardCharsets.UTF_8)));
    }

    public static class Config {

        private MessageDigest messageDigest;

        public MessageDigest getMessageDigest() {
            return messageDigest;
        }

        public void setAlgorithm(String algorithm) throws NoSuchAlgorithmException {
            messageDigest = MessageDigest.getInstance(algorithm);
        }
    }
}
