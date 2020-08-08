package com.hlebon.rest_template_test.service;

import lombok.AllArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class RestWrapper {

	private static final int PORT = 1080;
	private static final String HOST = "127.0.0.1";

	private final RestTemplate restTemplate;

	@Retryable(
			maxAttempts = 10
	)
	public String get() {
		return restTemplate.getForObject("http://" + HOST + ":" + PORT + "/validate", String.class);
	}

//	@Transactional
	public void generate() {
		// insert
		// update
		// delete - Error
		// insert
	}
}
