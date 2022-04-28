package com.livk.provider.api.feign.factory;

import com.livk.provider.api.domain.Users;
import com.livk.provider.api.feign.UserRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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
		return new UserRemoteService() {
			@Override
			public List<Users> users() {
				return Collections.emptyList();
			}

			@Override
			public Boolean save(Users users) {
				return false;
			}
		};
	}

}
