package com.livk.factory.gray

import kotlinx.atomicfu.atomic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.loadbalancer.DefaultResponse
import org.springframework.cloud.client.loadbalancer.EmptyResponse
import org.springframework.cloud.client.loadbalancer.Request
import org.springframework.cloud.client.loadbalancer.Response
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.util.CollectionUtils
import org.springframework.util.StringUtils
import reactor.core.publisher.Mono
import kotlin.math.abs
import kotlin.random.Random

/**
 * @author livk
 */
class GrayRoundRobinLoadBalancer(
    private val serviceInstanceListSupplierProvider: ObjectProvider<ServiceInstanceListSupplier>,
    private val serviceId: String
) : ReactorServiceInstanceLoadBalancer {
    private val position = atomic(Random.nextInt(1000))

    @Suppress("UNCHECKED_CAST")
    override fun choose(request: Request<*>): Mono<Response<ServiceInstance>> {
        val metaData = request.context as Map<String, List<String>>
        val supplier =
            serviceInstanceListSupplierProvider.getIfAvailable { NoopServiceInstanceListSupplier() }
        return supplier[request].next().map { list -> processInstanceResponse(list, metaData) }
    }

    private fun processInstanceResponse(
        serviceInstances: List<ServiceInstance>,
        metaData: Map<String, List<String>>
    ): Response<ServiceInstance> {
        val serviceInstanceResponse = this.getInstanceResponse(serviceInstances, metaData)
        if (serviceInstanceResponse is SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            (serviceInstanceResponse as SelectedInstanceCallback).selectedServiceInstance(serviceInstanceResponse.server)
        }
        return serviceInstanceResponse
    }

    /**
     * 获取可用实例
     *
     * @param instances 匹配serverId的应用列表
     * @param metaData  config数据
     * @return 可用server
     */
    private fun getInstanceResponse(
        instances: List<ServiceInstance>,
        metaData: Map<String, List<String>>
    ): Response<ServiceInstance> {
        val version = metaData[GrayConstant.VERSION]?.first()!!
        val ips = metaData[GrayConstant.IPS]!!
        //存放灰度server
        val grayInstanceServer: MutableList<ServiceInstance> = mutableListOf()
        //非灰度server
        val defaultInstanceServer: MutableList<ServiceInstance> = mutableListOf()
        for (serviceInstance in instances) {
            if (version == serviceInstance.metadata[GrayConstant.VERSION] && ips.contains(serviceInstance.host)) {
                grayInstanceServer.add(serviceInstance)
            } else {
                val metadata = serviceInstance.metadata
                //meta是空的也加入
                if (CollectionUtils.isEmpty(serviceInstance.metadata)) {
                    defaultInstanceServer.add(serviceInstance)
                } else {
                    //判断是否有版本号,有的话剔除,没有的话就加入到默认server中
                    if (!StringUtils.hasText(metadata[GrayConstant.VERSION])) {
                        defaultInstanceServer.add(serviceInstance)
                    }
                }
            }
        }
        val pos = abs(position.incrementAndGet())
        val instance: ServiceInstance
        if (version.isNotBlank()) {
            if (grayInstanceServer.isEmpty()) {
                if (log.isWarnEnabled) {
                    log.warn("请求灰度应用[{}]-对应版本[{}] 无可用路由", serviceId, version)
                }
                return EmptyResponse()
            }
            instance = grayInstanceServer[pos % grayInstanceServer.size]
        } else {
            if (defaultInstanceServer.isEmpty()) {
                if (log.isWarnEnabled) {
                    log.warn("请求应用[{}]无可用路由", serviceId)
                }
                return EmptyResponse()
            }
            instance = defaultInstanceServer[pos % defaultInstanceServer.size]
        }
        return DefaultResponse(instance)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(GrayRoundRobinLoadBalancer::class.java)
    }
}
