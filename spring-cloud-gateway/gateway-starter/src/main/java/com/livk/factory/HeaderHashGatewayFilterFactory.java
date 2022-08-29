package com.livk.factory;

import com.livk.util.JacksonUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * AuthGatewayFilterFactory
 * </p>
 *
 * @author livk
 * @date 2022/3/31
 */
@Component
public class HeaderHashGatewayFilterFactory extends AbstractGatewayFilterFactory<HeaderHashGatewayFilterFactory.Config> {

    private static final String HEADER_NAME = "X-Hash";
    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    public HeaderHashGatewayFilterFactory() {
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
                        .log()
                        .flatMap(body -> {
                            List<String> headerValueList = exchange.getRequest().getHeaders().get(HEADER_NAME);
                            if (!CollectionUtils.isEmpty(headerValueList) &&
                                headerValueList.contains(computeHash(config.getMessageDigest(), body))) {
                                return chain.filter(exchange);
                            }
                            ServerHttpResponse response = exchange.getResponse();
                            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                            response.setStatusCode(HttpStatus.OK);
                            Map<String, String> result = Map.of("code", "403", "msg", "缺少Header:X-Hash");
                            return response.writeWith(Mono.just(response.bufferFactory().wrap(JacksonUtils.toJsonStr(result).getBytes(StandardCharsets.UTF_8))));
                        })
                );
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
