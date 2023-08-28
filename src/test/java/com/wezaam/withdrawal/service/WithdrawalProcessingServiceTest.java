package com.wezaam.withdrawal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.model.dto.TransactionRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawalProcessingServiceTest {

	@InjectMocks
	WithdrawalProcessingService withdrawalProcessingService;

	@Mock
	EventsService eventsService;

	private Double amount = 10.0;
	private Double maxWithdrawalAmount = 100.0;
	private PaymentMethod testPaymentMethod = new PaymentMethod(1L, new User(), "some_payment_method");

	@Test
	void sendToProcessingSuccess() throws JsonProcessingException {
		assertThatCode(() -> withdrawalProcessingService.sendToProcessing(amount, testPaymentMethod, maxWithdrawalAmount)).doesNotThrowAnyException();
		verify(eventsService, times(1)).send(new TransactionRequestDto(amount, testPaymentMethod, Mockito.any()));
	}

	@Test
	void sendToProcessingAmountExceedsMaxWithdrawal() {
		Assertions.assertThrows(TransactionException.class, ()-> withdrawalProcessingService.sendToProcessing(maxWithdrawalAmount+1, testPaymentMethod, maxWithdrawalAmount));
	}

	@Test
	void sendToProcessingEventServiceError() throws JsonProcessingException {
		doThrow(new JsonProcessingException(""){}).when(eventsService).send(any(TransactionRequestDto.class));
		Assertions.assertThrows(TransactionException.class, ()-> withdrawalProcessingService.sendToProcessing(amount, testPaymentMethod, maxWithdrawalAmount));
		verify(eventsService, times(1)).send(new TransactionRequestDto(amount, testPaymentMethod, Mockito.any()));
	}
}