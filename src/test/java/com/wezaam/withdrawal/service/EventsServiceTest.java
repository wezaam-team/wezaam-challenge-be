package com.wezaam.withdrawal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.WithdrawalInstant;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import com.wezaam.withdrawal.model.dto.TransactionRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventsServiceTest {

	@InjectMocks
	EventsService eventsService;

	@Mock
	RabbitTemplate rabbitTemplate;
	@Mock
	ObjectMapper objectMapper;

	@Test
	void sendWithdrawalInstant() throws JsonProcessingException {
		when(objectMapper.writeValueAsString(any(WithdrawalInstant.class))).thenReturn("instant-withdrawal");
		eventsService.send(new WithdrawalInstant());
		verify(rabbitTemplate, times(1)).convertAndSend(EventsService.withdrawalInstantQueue, "instant-withdrawal");
	}

	@Test
	void sendWithdrawalScheduled() throws JsonProcessingException {
		when(objectMapper.writeValueAsString(any(WithdrawalScheduled.class))).thenReturn("scheduled-withdrawal");
		eventsService.send(new WithdrawalScheduled());
		verify(rabbitTemplate, times(1)).convertAndSend(EventsService.withdrawalScheduledQueue, "scheduled-withdrawal");
	}

	@Test
	void sendTransactionRequestDto() throws JsonProcessingException {
		when(objectMapper.writeValueAsString(any(TransactionRequestDto.class))).thenReturn("transaction-request");
		eventsService.send(new TransactionRequestDto(10.0, new PaymentMethod(), 1L));
		verify(rabbitTemplate, times(1)).convertAndSend(EventsService.transactionRequestQueue, "transaction-request");
	}
}