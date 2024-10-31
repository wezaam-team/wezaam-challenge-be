package com.wezaam.withdrawal.application.impl.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.domain.entity.Withdrawal;
import com.wezaam.withdrawal.domain.event.WithdrawalEvent;
import com.wezaam.withdrawal.domain.exception.TransactionException;
import com.wezaam.withdrawal.domain.ports.output.external.kafka.EventsService;
import com.wezaam.withdrawal.domain.ports.output.external.payment.WithdrawalProcessingService;
import com.wezaam.withdrawal.domain.ports.output.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalRepository;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

@ExtendWith(MockitoExtension.class)
class WithdrawalAsapTypeProcessorTest {

    @InjectMocks WithdrawalAsapTypeProcessor underTest;

    @Mock WithdrawalRepository withdrawalRepository;

    @Mock PaymentMethodRepository paymentMethodRepository;

    @Mock WithdrawalProcessingService withdrawalProcessingService;
    @Mock EventsService eventsService;

    @Captor ArgumentCaptor<Withdrawal> withdrawalArgumentCaptor;

    VerificationMode times1 = times(1);

    @Test
    void shouldProcessWithdrawalEventEmptyWithdrawal() {
        // arrange
        WithdrawalEvent event =
                WithdrawalEvent.builder()
                        .id(1L)
                        .paymentMethodId(1L)
                        .withdrawalType(WithdrawalType.ASAP)
                        .build();
        when(withdrawalRepository.findById(event.getId())).thenReturn(Optional.empty());

        // act
        underTest.execute(event);
        // asserts
        verify(withdrawalRepository, times1).findById(anyLong());
    }

    @Test
    void shouldProcessWithdrawalEventEmptyPaymentMethod() {
        // arrange
        WithdrawalEvent event =
                WithdrawalEvent.builder()
                        .id(1L)
                        .paymentMethodId(1L)
                        .withdrawalType(WithdrawalType.ASAP)
                        .build();
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setPaymentMethodId(event.getPaymentMethodId());
        when(withdrawalRepository.findById(event.getId())).thenReturn(Optional.of(withdrawal));
        when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.empty());

        // act
        underTest.execute(event);
        // asserts
        verify(withdrawalRepository, times1).findById(anyLong());
        verify(paymentMethodRepository, times1).findById(anyLong());
    }

    @Test
    void shouldProcessWithdrawalEventTransactionException() {

        // arrange
        WithdrawalEvent event =
                WithdrawalEvent.builder()
                        .id(1L)
                        .paymentMethodId(1L)
                        .withdrawalType(WithdrawalType.ASAP)
                        .build();
        PaymentMethod paymentMethod = getPaymentMethod();
        Withdrawal withdrawal = getWithdrawal();
        withdrawal.setPaymentMethodId(event.getPaymentMethodId());
        when(withdrawalRepository.findById(event.getId())).thenReturn(Optional.of(withdrawal));
        when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.of(paymentMethod));
        when(withdrawalProcessingService.sendToProcessing(anyDouble(), any(PaymentMethod.class)))
                .thenThrow(new TransactionException("Transaction error"));
        when(withdrawalRepository.save(any(Withdrawal.class)))
                .thenReturn(getWithdrawalCopy(withdrawal, null));

        // act
        underTest.execute(event);
        // asserts
        verify(withdrawalRepository, times1).findById(anyLong());
        verify(paymentMethodRepository, times1).findById(anyLong());
        verify(withdrawalRepository, times1).save(any(Withdrawal.class));
        verify(withdrawalProcessingService, times1)
                .sendToProcessing(anyDouble(), any(PaymentMethod.class));
        verify(eventsService, times1).send(withdrawalArgumentCaptor.capture());

        Withdrawal withdrawalSent = withdrawalArgumentCaptor.getValue();
        assertThat(withdrawalSent).isNotNull();
        assertThat(withdrawalSent.getStatus()).isNotNull().isEqualTo(WithdrawalStatus.FAILED);
        assertThat(withdrawalSent.getTransactionId()).isNull();
    }

    @Test
    void shouldProcessWithdrawalEventOtherException() {
        // arrange

        WithdrawalEvent event =
                WithdrawalEvent.builder()
                        .id(1L)
                        .paymentMethodId(1L)
                        .withdrawalType(WithdrawalType.ASAP)
                        .build();
        PaymentMethod paymentMethod = getPaymentMethod();
        Withdrawal withdrawal = getWithdrawal();
        withdrawal.setPaymentMethodId(event.getPaymentMethodId());
        when(withdrawalRepository.findById(event.getId())).thenReturn(Optional.of(withdrawal));
        when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.of(paymentMethod));
        when(withdrawalProcessingService.sendToProcessing(anyDouble(), any(PaymentMethod.class)))
                .thenThrow(new RuntimeException("Transaction error"));
        when(withdrawalRepository.save(any(Withdrawal.class)))
                .thenReturn(getWithdrawalCopy(withdrawal, null));

        // act
        underTest.execute(event);
        // asserts
        verify(withdrawalRepository, times1).findById(anyLong());
        verify(paymentMethodRepository, times1).findById(anyLong());
        verify(withdrawalRepository, times1).save(any(Withdrawal.class));
        verify(withdrawalProcessingService, times1)
                .sendToProcessing(anyDouble(), any(PaymentMethod.class));
        verify(eventsService, times1).send(withdrawalArgumentCaptor.capture());

        Withdrawal withdrawalSent = withdrawalArgumentCaptor.getValue();
        assertThat(withdrawalSent).isNotNull();
        assertThat(withdrawalSent.getStatus())
                .isNotNull()
                .isEqualTo(WithdrawalStatus.INTERNAL_ERROR);
        assertThat(withdrawalSent.getTransactionId()).isNull();
    }

    @Test
    void shouldProcessWithdrawalEvent() {
        // arrange
        Random random = new Random();
        final long transactionId = random.nextLong();
        WithdrawalEvent event =
                WithdrawalEvent.builder()
                        .id(1L)
                        .paymentMethodId(1L)
                        .withdrawalType(WithdrawalType.ASAP)
                        .build();
        PaymentMethod paymentMethod = getPaymentMethod();
        Withdrawal withdrawal = getWithdrawal();
        withdrawal.setPaymentMethodId(event.getPaymentMethodId());
        when(withdrawalRepository.findById(event.getId())).thenReturn(Optional.of(withdrawal));
        when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.of(paymentMethod));
        when(withdrawalProcessingService.sendToProcessing(anyDouble(), any(PaymentMethod.class)))
                .thenReturn(transactionId);
        when(withdrawalRepository.save(any(Withdrawal.class)))
                .thenReturn(getWithdrawalCopy(withdrawal, transactionId));

        // act
        underTest.execute(event);
        // asserts
        verify(withdrawalRepository, times1).findById(anyLong());
        verify(paymentMethodRepository, times1).findById(anyLong());
        verify(withdrawalRepository, times1).save(any(Withdrawal.class));
        verify(withdrawalProcessingService, times1)
                .sendToProcessing(anyDouble(), any(PaymentMethod.class));
        verify(eventsService, times1).send(withdrawalArgumentCaptor.capture());

        Withdrawal withdrawalSent = withdrawalArgumentCaptor.getValue();
        assertThat(withdrawalSent).isNotNull();
        assertThat(withdrawalSent.getTransactionId()).isNotNull().isEqualTo(transactionId);
        assertThat(withdrawalSent.getStatus()).isNotNull().isEqualTo(WithdrawalStatus.PROCESSING);
    }

    @Test
    void shouldGetType() {
        // act
        WithdrawalType type = underTest.getType();

        // asserts
        assertThat(type).isEqualTo(WithdrawalType.ASAP);
    }

    protected Withdrawal getWithdrawalCopy(Withdrawal withdrawal, Long transactionId) {
        Withdrawal copy = new Withdrawal();
        withdrawal.setId(withdrawal.getId());
        withdrawal.setPaymentMethodId(withdrawal.getPaymentMethodId());
        withdrawal.setAmount(withdrawal.getAmount());
        withdrawal.setStatus(WithdrawalStatus.PROCESSING);
        withdrawal.setCreatedAt(withdrawal.getCreatedAt());
        withdrawal.setUserId(withdrawal.getUserId());
        withdrawal.setTransactionId(transactionId);
        return copy;
    }

    protected Withdrawal getWithdrawal() {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(1L);
        withdrawal.setPaymentMethodId(1L);
        withdrawal.setAmount(500d);
        withdrawal.setStatus(WithdrawalStatus.PENDING);
        withdrawal.setCreatedAt(Instant.now());
        withdrawal.setUserId(1L);
        return withdrawal;
    }

    protected PaymentMethod getPaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setName("Bank account");
        return paymentMethod;
    }
}
