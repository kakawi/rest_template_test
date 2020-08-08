package com.hlebon.rest_template_test.rest;

import com.hlebon.rest_template_test.service.RestWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
class RestTemplateTestApplicationTests {


	private static final int PORT = 1080;
	private static final String HOST = "127.0.0.1";
	private ClientAndServer mockServer;

	@Autowired
	private RestWrapper restWrapper;

	@BeforeEach
	public void startServer() {
		mockServer = startClientAndServer(PORT);
	}

	@AfterEach
	public void stopServer() {
		mockServer.stop();
	}

	@Test
	void contextLoads() {
		MockServerClient mockServerClient = new MockServerClient(HOST, PORT);
		mockServerClient
				.when(
						request()
								.withMethod("GET")
								.withPath("/validate"),
						Times.exactly(5)
				)
				.respond(
						response()
								.withStatusCode(401)
								.withHeaders(
										new Header("Content-Type", "application/json; charset=utf-8"),
										new Header("Cache-Control", "public, max-age=86400")
								)
								.withBody("{ message: 'incorrect username and password combination' }")
								.withDelay(TimeUnit.SECONDS, 1)
				);

		mockServerClient
				.when(
						request()
								.withMethod("GET")
								.withPath("/validate")
				)
				.respond(
						response()
								.withStatusCode(200)
								.withHeaders(
										new Header("Content-Type", "application/json; charset=utf-8"),
										new Header("Cache-Control", "public, max-age=86400")
								)
								.withBody("{ message: 'incorrect username and password combination' }")
								.withDelay(TimeUnit.SECONDS, 1)
				);

		try {
			String body = restWrapper.get();
			System.out.println("GOOOD " + body);
		} catch (Exception e) {
			System.err.println("ERRROR " + e);
		}
	}

}
