package com.livk.provider;


import com.livk.support.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ProviderController
 * </p>
 *
 * @author livk
 * @date 2022/4/13
 */
@RestController
@RequiredArgsConstructor
public class ProviderController {

    private final DiscoveryClient discoveryClient;

    @GetMapping("instance")
    public HttpEntity<String> instance() {
        return ResponseEntity.ok(discoveryClient
                .getInstances(SpringContextHolder.getApplicationContext().getEnvironment()
                        .getProperty("spring.application.name"))
                .stream().findFirst().map(ServiceInstance::getInstanceId).orElse("is null"));
    }

}
