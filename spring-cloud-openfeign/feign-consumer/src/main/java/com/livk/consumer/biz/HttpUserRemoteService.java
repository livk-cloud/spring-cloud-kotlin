package com.livk.consumer.biz;

import com.livk.autoconfigure.http.annotation.HttpProvider;
import com.livk.provider.api.domain.Users;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

/**
 * @author livk
 */
@HttpProvider(value = "httpUserRemoteService", url = "http://feign-provider-biz")
public interface HttpUserRemoteService {

    @GetExchange("/users")
    List<Users> users();
}
