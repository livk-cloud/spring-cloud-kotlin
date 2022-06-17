package com.livk.filter;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * ResultHandlerWebFilter
 * </p>
 *
 * @author livk
 * @date 2022/5/10
 */
public abstract class ResultHandlerWebFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var originalResponse = exchange.getResponse();
        if (chooseHandler(originalResponse)) {
            var decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        @SuppressWarnings("unchecked")
                        var fluxBody = (Flux<? extends DataBuffer>) body;
                        return super.writeWith(fluxBody.map(dataBuffer -> {
                            var content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);
                            var result = new String(content, StandardCharsets.UTF_8);
                            return originalResponse.bufferFactory()
                                    .wrap(resultHandler(result).getBytes(StandardCharsets.UTF_8));
                        }));
                    }
                    return super.writeWith(body);
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        }
        return chain.filter(exchange);
    }

    protected abstract boolean chooseHandler(ServerHttpResponse response);

    protected abstract String resultHandler(String result);

}
