package com.livk.factory.gray;

import com.livk.util.JacksonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * GrayGatewayFilterFactory
 * </p>
 *
 * @author livk
 * @date 2022/9/27
 */
@Slf4j
public class GrayGatewayFilterFactory extends AbstractGatewayFilterFactory<GrayGatewayFilterFactory.Config> {

    private static final String GRAY_LB = "gray-lb";
    private final LoadBalancerClientFactory clientFactory;

    public GrayGatewayFilterFactory(LoadBalancerClientFactory clientFactory) {
        super(Config.class);
        this.clientFactory = clientFactory;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            Route route = getRoute(exchange);
            URI uri = route.getUri();
            String schemePrefix = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);
            if (uri == null || (!GRAY_LB.equals(uri.getScheme()) && !GRAY_LB.equals(schemePrefix))) {
                return chain.filter(exchange);
            }
            ServerWebExchangeUtils.addOriginalRequestUrl(exchange, uri);
            HttpHeaders headers = exchange.getRequest().getHeaders();
            List<String> headerVersions = headers.get(GrayConstant.VERSION);
            if (CollectionUtils.isEmpty(headerVersions)) {
                return out(exchange, "缺少Gray关键字");
            }
            if (!headerVersions.contains(config.getVersion())) {
                return out(exchange, "version不匹配!");
            }
            GrayRoundRobinLoadBalancer loadBalancer = new GrayRoundRobinLoadBalancer(clientFactory.getLazyProvider(uri.getHost(), ServiceInstanceListSupplier.class), uri.getHost());
            Map<String, List<String>> metaData = Map.of(GrayConstant.VERSION, List.of(config.getVersion()), GrayConstant.IPS, config.getIps());
            return loadBalancer.choose(new DefaultRequest<>(metaData))
                    .doOnNext(serviceInstanceResponse -> {
                        if (!serviceInstanceResponse.hasServer()) {
                            throw NotFoundException.create(false, "Unable to find instance for " + uri.getHost());
                        } else {
                            URI requestUri = exchange.getRequest().getURI();
                            String overrideScheme = null;
                            if (schemePrefix != null) {
                                overrideScheme = requestUri.getScheme();
                            }
                            DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(serviceInstanceResponse.getServer(), overrideScheme);
                            URI requestUrl = reconstructURI(serviceInstance, requestUri);
                            Route newRoute = Route.async()
                                    .asyncPredicate(route.getPredicate())
                                    .id(route.getId())
                                    .order(route.getOrder())
                                    .uri(requestUrl)
                                    .build();
                            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, newRoute);
                        }
                    }).then(chain.filter(exchange));
        };
    }

    private Route getRoute(ServerWebExchange exchange) {
        Object attribute = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (attribute instanceof Route route) {
            return route;
        }
        throw new NotFoundException("丢失route!");
    }

    private URI reconstructURI(ServiceInstance serviceInstance, URI original) {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }

    public Mono<Void> out(ServerWebExchange exchange, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);
        Map<String, String> result = Map.of("code", "503", "msg", msg);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(JacksonUtils.toJsonStr(result).getBytes(StandardCharsets.UTF_8))));
    }

    @Setter
    @Getter
    public static class Config {
        private String version;

        private List<String> ips;
    }
}
