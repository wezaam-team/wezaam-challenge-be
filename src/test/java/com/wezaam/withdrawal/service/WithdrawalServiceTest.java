package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.model.WithdrawalInstant;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import com.wezaam.withdrawal.repository.WithdrawalRepository;
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawalServiceTest {

	@Spy
	@InjectMocks
	WithdrawalService withdrawalService;

	@Mock
	WithdrawalRepository withdrawalRepository;
	@Mock
	WithdrawalScheduledRepository withdrawalScheduledRepository;
	@Mock
	WithdrawalProcessingService withdrawalProcessingService;
	@Mock
	EventsService eventsService;
	@Mock
	UserService userService;
	@Mock
	PaymentMethodService paymentMethodService;

	private WithdrawalScheduled withdrawalScheduled = new WithdrawalScheduled();
	private WithdrawalInstant withdrawalInstant = new WithdrawalInstant();
	private User testUser1 = new User(1L, "test_name1", null, 100.0);


	@Test
	void isValidWithdrawalUser() {
		when(userService.findById(testUser1.getId())).thenReturn(testUser1);
		assertTrue(withdrawalService.isValidWithdrawalUser(testUser1.getId()));
	}

	@Test
	void isInvalidWithdrawalUser() {
		when(userService.findById(testUser1.getId())).thenThrow(new NoSuchElementException());
		assertFalse(withdrawalService.isValidWithdrawalUser(testUser1.getId()));
	}

	@Test
	void isValidWithdrawalPaymentMethod() {
		when(paymentMethodService.findById(1L)).thenReturn(new PaymentMethod());
		assertTrue(withdrawalService.isValidWithdrawalPaymentMethod(1L));
	}

	@Test
	void isInvalidWithdrawalPaymentMethod() {
		when(paymentMethodService.findById(0L)).thenThrow(new NoSuchElementException());
		assertFalse(withdrawalService.isValidWithdrawalPaymentMethod(0L));
	}

	@Test
	void handleWithdrawalInstant() {
		assertThatCode(() -> withdrawalService.handleWithdrawal(withdrawalInstant)).doesNotThrowAnyException();
		verify(withdrawalService, times(1)).create(withdrawalInstant);
	}

	@Test
	void handleWithdrawalScheduled() {
		assertThatCode(() -> withdrawalService.handleWithdrawal(withdrawalScheduled)).doesNotThrowAnyException();
		verify(withdrawalService, times(1)).schedule(withdrawalScheduled);
	}

	@Test
	void createSuccess() throws TransactionException, InterruptedException {
		when(withdrawalRepository.save(withdrawalInstant)).thenReturn(withdrawalInstant);
		when(userService.findById(withdrawalInstant.getUserId())).thenReturn(testUser1);
		when(paymentMethodService.findById(withdrawalInstant.getPaymentMethodId())).thenReturn(new PaymentMethod());
		withdrawalService.create(withdrawalInstant);
		Thread.sleep(100);
		verify(withdrawalProcessingService, times(1)).sendToProcessing(withdrawalInstant.getAmount(), paymentMethodService.findById(withdrawalInstant.getPaymentMethodId()), userService.findById(withdrawalInstant.getUserId()).getMaxWithdrawalAmount());
		verify(withdrawalRepository, times(2)).save(any(WithdrawalInstant.class));
		verify(eventsService, times(1)).send(any(WithdrawalInstant.class));
	}

	@Test
	void createContinueIfExceptionThrown() throws TransactionException, InterruptedException {
		when(withdrawalRepository.save(withdrawalInstant)).thenReturn(withdrawalInstant);
		when(userService.findById(withdrawalInstant.getUserId())).thenReturn(testUser1);
		when(paymentMethodService.findById(withdrawalInstant.getPaymentMethodId())).thenReturn(new PaymentMethod());
		when(withdrawalProcessingService.sendToProcessing(withdrawalInstant.getAmount(), paymentMethodService.findById(withdrawalInstant.getPaymentMethodId()), userService.findById(withdrawalInstant.getUserId()).getMaxWithdrawalAmount())).thenThrow(new TransactionException("Some error"));
		withdrawalService.create(withdrawalInstant);
		Thread.sleep(100);
		verify(withdrawalProcessingService, times(1)).sendToProcessing(withdrawalInstant.getAmount(), paymentMethodService.findById(withdrawalInstant.getPaymentMethodId()), userService.findById(withdrawalInstant.getUserId()).getMaxWithdrawalAmount());
		verify(withdrawalRepository, times(2)).save(any(WithdrawalInstant.class));
		verify(eventsService, times(1)).send(any(WithdrawalInstant.class));
	}

	@Test
	void schedule() {
		when(withdrawalScheduledRepository.save(withdrawalScheduled)).thenReturn(withdrawalScheduled);
		withdrawalService.schedule(withdrawalScheduled);
		verify(withdrawalScheduledRepository, times(1)).save(any(WithdrawalScheduled.class));
	}

	@Test
	void run() {
		withdrawalService.run();
		verify(withdrawalScheduledRepository, times(1)).findAllByExecuteAtBeforeAndStatusEquals(any(), any());
	}

	@Test
	void findAllWithdrawals() {
		withdrawalService.findAllWithdrawals();
		verify(withdrawalRepository, times(1)).findAll();
	}

	@Test
	void findAllWithdrawalsScheduled() {
		withdrawalService.findAllWithdrawalsScheduled();
		verify(withdrawalScheduledRepository, times(1)).findAll();
	}
}