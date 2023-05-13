package com.livk.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.commons.jackson.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.LimitedDataBufferList;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
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
@Slf4j
@Component
@SpringAutoService
public class ResultHandlerWebFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponse decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @NonNull
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                Mono<DataBuffer> dataBufferFlux = Flux.from(body).collect(() -> new LimitedDataBufferList(-1), LimitedDataBufferList::add)
                        .filter((list) -> !list.isEmpty()).map((list) -> list.get(0).factory().join(list))
                        .doOnDiscard(DataBuffer.class, DataBufferUtils::release)
                        .map(dataBuffer -> {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);
                            String result = new String(content, StandardCharsets.UTF_8);

                            byte[] bytes = resultHandler(result);
                            getHeaders().setContentLength(bytes.length);
                            getHeaders().setContentType(MediaType.APPLICATION_JSON);
                            return bufferFactory.wrap(bytes);
                        });
                return super.writeWith(dataBufferFlux);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    protected byte[] resultHandler(String result) {
        JsonNode jsonNode = JacksonUtils.readTree(result);
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("code", 200)
                .put("msg", "ok")
                .set("data", jsonNode);
        return JacksonUtils.writeValueAsBytes(node);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
