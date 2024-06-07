package com.livk.factory.gray

import com.livk.commons.jackson.util.JsonMapperUtils
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.loadbalancer.DefaultRequest
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools
import org.springframework.cloud.client.loadbalancer.Response
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.support.DelegatingServiceInstance
import org.springframework.cloud.gateway.support.NotFoundException
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.CollectionUtils
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.net.URI
import java.nio.charset.StandardCharsets

/**
 * @author livk
 */
class GrayGatewayFilterFactory(private val clientFactory: LoadBalancerClientFactory) :
    AbstractGatewayFilterFactory<GrayGatewayFilterFactory.Config>(
        Config::class.java
    ) {
    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val route = getRoute(exchange)
            val uri = route.uri
            val schemePrefix = exchange.getAttribute<String>(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR)
            if (uri == null || (GRAY_LB != uri.scheme && GRAY_LB != schemePrefix)) {
                return@GatewayFilter chain.filter(exchange)
            }
            ServerWebExchangeUtils.addOriginalRequestUrl(exchange, uri)
            val headers = exchange.request.headers
            val headerVersions = headers[GrayConstant.VERSION]
            if (CollectionUtils.isEmpty(headerVersions)) {
                return@GatewayFilter out(exchange, "缺少Gray关键字")
            }
            if (!headerVersions!!.contains(config.version)) {
                return@GatewayFilter out(exchange, "version不匹配!")
            }
            val loadBalancer = GrayRoundRobinLoadBalancer(
                clientFactory.getLazyProvider(
                    uri.host,
                    ServiceInstanceListSupplier::class.java
                ), uri.host
            )
            val metaData = mapOf(
                GrayConstant.VERSION to listOf(config.version), GrayConstant.IPS to config.ips
            )
            loadBalancer.choose(DefaultRequest(metaData))
                .doOnNext { serviceInstanceResponse: Response<ServiceInstance> ->
                    if (!serviceInstanceResponse.hasServer()) {
                        throw NotFoundException.create(false, "Unable to find instance for " + uri.host)
                    } else {
                        val requestUri = exchange.request.uri
                        var overrideScheme: String? = null
                        if (schemePrefix != null) {
                            overrideScheme = requestUri.scheme
                        }
                        val serviceInstance = DelegatingServiceInstance(serviceInstanceResponse.server, overrideScheme)
                        val requestUrl = reconstructURI(serviceInstance, requestUri)
                        val newRoute = Route.async()
                            .asyncPredicate(route.predicate)
                            .id(route.id)
                            .order(route.order)
                            .uri(requestUrl)
                            .build()
                        exchange.attributes[ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR] = newRoute
                    }
                }.then(chain.filter(exchange))
        }
    }

    private fun getRoute(exchange: ServerWebExchange): Route {
        val attribute = exchange.getAttribute<Any>(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR)
        if (attribute is Route) {
            return attribute
        }
        throw NotFoundException("丢失route!")
    }

    private fun reconstructURI(serviceInstance: ServiceInstance, original: URI): URI {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original)
    }

    fun out(exchange: ServerWebExchange, msg: String): Mono<Void> {
        val response = exchange.response
        response.headers.contentType = MediaType.APPLICATION_JSON
        response.setStatusCode(HttpStatus.OK)
        val result = mapOf("code" to "503", "msg" to msg)
        return response.writeWith(
            Mono.just(
                response.bufferFactory().wrap(
                    JsonMapperUtils.writeValueAsString(result).toByteArray(
                        StandardCharsets.UTF_8
                    )
                )
            )
        )
    }

    class Config {
        lateinit var version: String

        lateinit var ips: List<String>
    }

    companion object {
        private const val GRAY_LB = "gray-lb"
    }
}
