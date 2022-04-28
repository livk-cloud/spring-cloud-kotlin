package com.livk.consumer;

import lombok.RequiredArgsConstructor;
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

	private final RemoteService remoteService;

	@GetMapping("/remote/instance")
	public HttpEntity<String> remoteInstance() throws IOException {
		return ResponseEntity.ok(remoteService.instance().execute().body());
	}

}
