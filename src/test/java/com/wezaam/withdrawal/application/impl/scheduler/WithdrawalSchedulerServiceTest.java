package com.wezaam.withdrawal.application.impl.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import com.wezaam.withdrawal.application.WithdrawalProcessorService;
import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.event.WithdrawalEvent;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

@ExtendWith(MockitoExtension.class)
class WithdrawalSchedulerServiceTest {

    @InjectMocks WithdrawalSchedulerServiceImpl underTest;

    @Mock WithdrawalScheduledRepository withdrawalScheduledRepository;

    @Mock WithdrawalProcessorService withdrawalProcessorService;

    @Captor ArgumentCaptor<WithdrawalEvent> withdrawalEventArgumentCaptor;

    VerificationMode times1 = times(1);

    @BeforeEach
    public void init() {}

    @Test
    void shouldRunSchedulerEmpty() {

        // arrange
        when(withdrawalScheduledRepository.findAllByExecuteAtBeforeWithStatus(
                        any(Instant.class), any(WithdrawalStatus.class)))
                .thenReturn(List.of());

        // act
        underTest.run();

        // asserts
        verify(withdrawalScheduledRepository, times1)
                .findAllByExecuteAtBeforeWithStatus(
                        any(Instant.class), any(WithdrawalStatus.class));
    }

    @Test
    void shouldRunScheduler() {

        // arrange
        when(withdrawalScheduledRepository.findAllByExecuteAtBeforeWithStatus(
                        any(Instant.class), any(WithdrawalStatus.class)))
                .thenReturn(List.of(getWithdrawalScheduled()));

        // act
        underTest.run();

        // asserts
        verify(withdrawalScheduledRepository, times1)
                .findAllByExecuteAtBeforeWithStatus(
                        any(Instant.class), any(WithdrawalStatus.class));
        verify(withdrawalProcessorService, times1).process(withdrawalEventArgumentCaptor.capture());
        WithdrawalEvent withdrawalEvent = withdrawalEventArgumentCaptor.getValue();
        assertThat(withdrawalEvent).isNotNull();
        assertThat(withdrawalEvent.getId()).isNotNull();
        assertThat(withdrawalEvent.getWithdrawalType())
                .isNotNull()
                .isEqualTo(WithdrawalType.SCHEDULED);
    }

    protected WithdrawalScheduled getWithdrawalScheduled() {
        WithdrawalScheduled withdrawalScheduled = new WithdrawalScheduled();
        withdrawalScheduled.setId(1L);
        withdrawalScheduled.setStatus(WithdrawalStatus.PENDING);
        withdrawalScheduled.setAmount(300D);
        withdrawalScheduled.setUserId(1L);
        withdrawalScheduled.setPaymentMethodId(1L);
        withdrawalScheduled.setCreatedAt(Instant.now());
        withdrawalScheduled.setExecuteAt(Instant.now().plus(10000, ChronoUnit.SECONDS));
        return withdrawalScheduled;
    }
}
