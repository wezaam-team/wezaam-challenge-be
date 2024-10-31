package com.wezaam.withdrawal.application.impl.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

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
import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.event.WithdrawalEvent;
import com.wezaam.withdrawal.domain.exception.TransactionException;
import com.wezaam.withdrawal.domain.ports.output.external.kafka.EventsService;
import com.wezaam.withdrawal.domain.ports.output.external.payment.WithdrawalProcessingService;
import com.wezaam.withdrawal.domain.ports.output.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

@ExtendWith(MockitoExtension.class)
class WithdrawalScheduleTypeProcessorTest {

    @InjectMocks WithdrawalScheduleTypeProcessor underTest;

    @Mock WithdrawalScheduledRepository withdrawalScheduledRepository;

    @Mock PaymentMethodRepository paymentMethodRepository;

    @Mock WithdrawalProcessingService withdrawalProcessingService;
    @Mock EventsService eventsService;

    @Captor ArgumentCaptor<WithdrawalScheduled> withdrawalScheduledArgumentCaptor;

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
        when(withdrawalScheduledRepository.findById(event.getId())).thenReturn(Optional.empty());

        // act
        underTest.execute(event);
        // asserts
        verify(withdrawalScheduledRepository, times1).findById(anyLong());
        assertThat(withdrawalScheduledRepository).isNotNull();
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
        WithdrawalScheduled withdrawalScheduled = new WithdrawalScheduled();
        withdrawalScheduled.setPaymentMethodId(event.getPaymentMethodId());
        when(withdrawalScheduledRepository.findById(event.getId()))
                .thenReturn(Optional.of(withdrawalScheduled));
        when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.empty());

        // act
        underTest.execute(event);
        // asserts
        verify(withdrawalScheduledRepository, times1).findById(anyLong());
        verify(paymentMethodRepository, times1).findById(anyLong());
        assertThat(withdrawalScheduledRepository).isNotNull();
        assertThat(paymentMethodRepository).isNotNull();
    }

    @Test
    void shouldProcessWithdrawalScheduledEventTransactionException() {

        // arrange
        WithdrawalEvent event =
                WithdrawalEvent.builder()
                        .id(1L)
                        .paymentMethodId(1L)
                        .withdrawalType(WithdrawalType.ASAP)
                        .build();
        PaymentMethod paymentMethod = getPaymentMethod();
        WithdrawalScheduled withdrawalScheduled = getWithdrawalScheduled();
        withdrawalScheduled.setPaymentMethodId(event.getPaymentMethodId());
        when(withdrawalScheduledRepository.findById(event.getId()))
                .thenReturn(Optional.of(withdrawalScheduled));
        when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.of(paymentMethod));
        when(withdrawalProcessingService.sendToProcessing(anyDouble(), any(PaymentMethod.class)))
                .thenThrow(new TransactionException("Transaction error"));
        when(withdrawalScheduledRepository.save(any(WithdrawalScheduled.class)))
                .thenReturn(getWithdrawalScheduledCopy(withdrawalScheduled, null));

        // act
        underTest.execute(event);
        // asserts
        verify(withdrawalScheduledRepository, times1).findById(anyLong());
        verify(paymentMethodRepository, times1).findById(anyLong());
        verify(withdrawalScheduledRepository, times1).save(any(WithdrawalScheduled.class));
        verify(withdrawalProcessingService, times1)
                .sendToProcessing(anyDouble(), any(PaymentMethod.class));
        verify(eventsService, times1).send(withdrawalScheduledArgumentCaptor.capture());

        WithdrawalScheduled withdrawalScheduledSent = withdrawalScheduledArgumentCaptor.getValue();
        assertThat(withdrawalScheduledSent).isNotNull();
        assertThat(withdrawalScheduledSent.getStatus())
                .isNotNull()
                .isEqualTo(WithdrawalStatus.FAILED);
        assertThat(withdrawalScheduledSent.getTransactionId()).isNull();
    }

    @Test
    void shouldProcessWithdrawalScheduledEventOtherException() {
        // arrange

        WithdrawalEvent event =
                WithdrawalEvent.builder()
                        .id(1L)
                        .paymentMethodId(1L)
                        .withdrawalType(WithdrawalType.ASAP)
                        .build();
        PaymentMethod paymentMethod = getPaymentMethod();
        WithdrawalScheduled withdrawalScheduled = getWithdrawalScheduled();
        withdrawalScheduled.setPaymentMethodId(event.getPaymentMethodId());
        when(withdrawalScheduledRepository.findById(event.getId()))
                .thenReturn(Optional.of(withdrawalScheduled));
        when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.of(paymentMethod));
        when(withdrawalProcessingService.sendToProcessing(anyDouble(), any(PaymentMethod.class)))
                .thenThrow(new RuntimeException("Transaction error"));
        when(withdrawalScheduledRepository.save(any(WithdrawalScheduled.class)))
                .thenReturn(getWithdrawalScheduledCopy(withdrawalScheduled, null));

        // act
        underTest.execute(event);
        // asserts
        verify(withdrawalScheduledRepository, times1).findById(anyLong());
        verify(paymentMethodRepository, times1).findById(anyLong());
        verify(withdrawalScheduledRepository, times1).save(any(WithdrawalScheduled.class));
        verify(withdrawalProcessingService, times1)
                .sendToProcessing(anyDouble(), any(PaymentMethod.class));
        verify(eventsService, times1).send(withdrawalScheduledArgumentCaptor.capture());

        WithdrawalScheduled withdrawalScheduledSent = withdrawalScheduledArgumentCaptor.getValue();
        assertThat(withdrawalScheduledSent).isNotNull();
        assertThat(withdrawalScheduledSent.getStatus())
                .isNotNull()
                .isEqualTo(WithdrawalStatus.INTERNAL_ERROR);
        assertThat(withdrawalScheduledSent.getTransactionId()).isNull();
    }

    @Test
    void shouldProcessWithdrawalScheduledEvent() {
        // arrange
        Random random = new Random();
        final long transactionId = random.nextLong();
        WithdrawalEvent event =
                WithdrawalEvent.builder()
                        .id(1L)
                        .paymentMethodId(1L)
                        .withdrawalType(WithdrawalType.SCHEDULED)
                        .build();
        PaymentMethod paymentMethod = getPaymentMethod();
        WithdrawalScheduled withdrawalScheduled = getWithdrawalScheduled();
        withdrawalScheduled.setPaymentMethodId(event.getPaymentMethodId());
        when(withdrawalScheduledRepository.findById(event.getId()))
                .thenReturn(Optional.of(withdrawalScheduled));
        when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.of(paymentMethod));
        when(withdrawalProcessingService.sendToProcessing(anyDouble(), any(PaymentMethod.class)))
                .thenReturn(transactionId);
        when(withdrawalScheduledRepository.save(any(WithdrawalScheduled.class)))
                .thenReturn(getWithdrawalScheduledCopy(withdrawalScheduled, transactionId));

        // act
        underTest.execute(event);
        // asserts
        verify(withdrawalScheduledRepository, times1).findById(anyLong());
        verify(paymentMethodRepository, times1).findById(anyLong());
        verify(withdrawalScheduledRepository, times1).save(any(WithdrawalScheduled.class));
        verify(withdrawalProcessingService, times1)
                .sendToProcessing(anyDouble(), any(PaymentMethod.class));
        verify(eventsService, times1).send(withdrawalScheduledArgumentCaptor.capture());

        WithdrawalScheduled withdrawalScheduledSent = withdrawalScheduledArgumentCaptor.getValue();
        assertThat(withdrawalScheduledSent).isNotNull();
        assertThat(withdrawalScheduledSent.getTransactionId()).isNotNull().isEqualTo(transactionId);
        assertThat(withdrawalScheduledSent.getStatus())
                .isNotNull()
                .isEqualTo(WithdrawalStatus.PROCESSING);
    }

    @Test
    void shouldGetType() {
        // act
        WithdrawalType type = underTest.getType();

        // asserts
        assertThat(type).isEqualTo(WithdrawalType.SCHEDULED);
    }

    protected WithdrawalScheduled getWithdrawalScheduledCopy(
            WithdrawalScheduled withdrawalScheduled, Long transactionId) {
        WithdrawalScheduled copy = new WithdrawalScheduled();
        withdrawalScheduled.setId(withdrawalScheduled.getId());
        withdrawalScheduled.setPaymentMethodId(withdrawalScheduled.getPaymentMethodId());
        withdrawalScheduled.setAmount(withdrawalScheduled.getAmount());
        withdrawalScheduled.setStatus(WithdrawalStatus.PROCESSING);
        withdrawalScheduled.setCreatedAt(withdrawalScheduled.getCreatedAt());
        withdrawalScheduled.setExecuteAt(withdrawalScheduled.getExecuteAt());
        withdrawalScheduled.setUserId(withdrawalScheduled.getUserId());
        withdrawalScheduled.setTransactionId(transactionId);
        return copy;
    }

    protected WithdrawalScheduled getWithdrawalScheduled() {
        WithdrawalScheduled withdrawalScheduled = new WithdrawalScheduled();
        withdrawalScheduled.setId(1L);
        withdrawalScheduled.setPaymentMethodId(1L);
        withdrawalScheduled.setAmount(500d);
        withdrawalScheduled.setStatus(WithdrawalStatus.PENDING);
        withdrawalScheduled.setCreatedAt(Instant.now());
        withdrawalScheduled.setExecuteAt(Instant.now());
        withdrawalScheduled.setUserId(1L);
        return withdrawalScheduled;
    }

    protected PaymentMethod getPaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setName("Bank account");
        return paymentMethod;
    }
}
