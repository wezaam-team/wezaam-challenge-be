package com.wezaam.withdrawal.controller;

import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerTest {

	@Value(value="${local.server.port}")
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void findAll() {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/find-all-users", List.class)).size().isEqualTo(3);
	}

	@Test
	void findById() {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/find-user-by-id/1", User.class)).isNotNull();
	}
}