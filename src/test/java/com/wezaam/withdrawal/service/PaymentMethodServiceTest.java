package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceTest {

	@InjectMocks
	PaymentMethodService paymentMethodService;

	@Mock
	PaymentMethodRepository paymentMethodRepository;

	private PaymentMethod testPaymentMethod = new PaymentMethod(1L, new User(), "some_payment_method");

	@Test
	void findById() {
		when(paymentMethodRepository.findById(testPaymentMethod.getId())).thenReturn(Optional.ofNullable(testPaymentMethod));
		Assertions.assertEquals(testPaymentMethod, paymentMethodService.findById(testPaymentMethod.getId()));
	}
}