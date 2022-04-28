package com.livk.consumer;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * <p>
 * RemoteController
 * </p>
 *
 * @author livk
 * @date 2022/4/13
 */
@RestController
@RequiredArgsConstructor
public class RemoteController {

	private final OkHttpClient.Builder builder;

	// 本质就是使用okhttp调用，和restTemplate没什么区别
	@GetMapping("/remote/instance")
	public HttpEntity<String> remoteInstance() throws IOException {
		Request request = new Request.Builder().url("http://square-provider/instance").build();
		try (Response response = builder.build().newCall(request).execute()) {
			return ResponseEntity.ok(response.body().string());
		}
	}

}
