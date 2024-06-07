package com.livk.consumer

import org.springframework.cloud.square.retrofit.core.RetrofitClient
import reactor.core.publisher.Mono
import retrofit2.http.GET

/**
 * @author livk
 */
@RetrofitClient("square-provider")
interface RemoteService {
    @GET("instance")
    fun instance(): Mono<String>
}
