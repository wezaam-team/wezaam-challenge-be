package com.wezaam.withdrawal.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wezaam.withdrawal.model.dto.WithdrawalScheduledDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;


@SpringBootTest
@AutoConfigureMockMvc
class WithdrawalControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void create() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		WithdrawalScheduledDto withdrawalSchedule = new WithdrawalScheduledDto(1L, 1L, 50.0, Instant.now());
		this.mockMvc.perform(post("/create-withdrawals").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(withdrawalSchedule))).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void findAll() throws Exception {
		this.mockMvc.perform(get("/find-all-withdrawals")).andDo(print()).andExpect(status().isOk());
	}
}