package com.livk.provider.api.feign.factory;

import com.livk.provider.api.feign.UserRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * <p>
 * UserRemoteServiceFallbackImpl
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@Slf4j
@Component
public class UserRemoteServiceFallbackFactory implements FallbackFactory<UserRemoteService> {
    @Override
    public UserRemoteService create(Throwable cause) {
        return null;
    }
}
