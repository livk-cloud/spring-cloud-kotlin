package com.livk.factory;

import com.livk.util.FieldUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * GrayRoundRobinLoadBalancer
 * </p>
 *
 * @author livk
 * @date 2022/9/27
 */
@Slf4j
public class GrayRoundRobinLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    //对应用户请求的Key
    private static final String GaryKey = "GRAY_VERSION";
    //对应灰度应用metadata中的Key
    private static final String GRAY_META = "GRAY_META";


    private final AtomicInteger position;
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final String serviceId;

    public GrayRoundRobinLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.position = new AtomicInteger(new Random().nextInt(1000));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mono<Response<ServiceInstance>> choose(@SuppressWarnings("rawtypes") Request request) {
        Map<String, List<String>> metaData = (Map<String, List<String>>) request.getContext();
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(list -> processInstanceResponse(list, metaData));
    }

    private Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> serviceInstances, Map<String, List<String>> metaData) {
        Response<ServiceInstance> serviceInstanceResponse = this.getInstanceResponse(serviceInstances, metaData);
        if (serviceInstanceResponse instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) serviceInstanceResponse).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    /**
     * 获取可用实例
     *
     * @param instances 匹配serverId的应用列表
     * @param metaData  config数据
     * @return 可用server
     */
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, Map<String, List<String>> metaData) {
        String version = metaData.get(FieldUtils.getFieldName(GrayGatewayFilterFactory.Config::getVersion)).get(0);
        List<String> ips = metaData.get(FieldUtils.getFieldName(GrayGatewayFilterFactory.Config::getIps));
        //存放灰度server
        List<ServiceInstance> grayInstanceServer = new ArrayList<>();
        //非灰度server
        List<ServiceInstance> defaultInstanceServer = new ArrayList<>();
        for (ServiceInstance serviceInstance : instances) {
            if (version.equals(serviceInstance.getMetadata().get(GRAY_META)) && ips.contains(serviceInstance.getHost())) {
                grayInstanceServer.add(serviceInstance);
            } else {
                Map<String, String> metadata = serviceInstance.getMetadata();
                //meta是空的也加入
                if (CollectionUtils.isEmpty(serviceInstance.getMetadata())) {
                    defaultInstanceServer.add(serviceInstance);
                } else {
                    //判断是否有版本号,有的话剔除,没有的话就加入到默认server中
                    if (!StringUtils.hasText(metadata.get(GaryKey))) {
                        defaultInstanceServer.add(serviceInstance);
                    }
                }
            }
        }
        int pos = Math.abs(this.position.incrementAndGet());
        ServiceInstance instance;
        if (StringUtils.hasText(version)) {
            if (grayInstanceServer.isEmpty()) {
                if (log.isWarnEnabled()) {
                    log.warn("请求灰度应用[{}]-对应版本[{}] 无可用路由", serviceId, version);
                }
                return new EmptyResponse();
            }
            instance = grayInstanceServer.get(pos % grayInstanceServer.size());
        } else {
            if (defaultInstanceServer.isEmpty()) {
                if (log.isWarnEnabled()) {
                    log.warn("请求应用[{}]无可用路由", serviceId);
                }
                return new EmptyResponse();
            }
            instance = defaultInstanceServer.get(pos % defaultInstanceServer.size());
        }
        return new DefaultResponse(instance);
    }
}
