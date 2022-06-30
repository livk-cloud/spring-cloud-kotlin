package com.livk.consumer;

import org.springframework.cloud.square.retrofit.core.RetrofitClient;
import reactor.core.publisher.Mono;
import retrofit2.http.GET;

/**
 * <p>
 * RemoteService
 * </p>
 *
 * @author livk
 * @date 2022/4/13
 */
@RetrofitClient("square-provider")
public interface RemoteService {

    @GET("instance")
    Mono<String> instance();

}
