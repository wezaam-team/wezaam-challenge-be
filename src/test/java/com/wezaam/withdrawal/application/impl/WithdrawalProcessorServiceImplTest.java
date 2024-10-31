package com.wezaam.withdrawal.application.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import com.wezaam.withdrawal.application.impl.processor.*;
import com.wezaam.withdrawal.domain.event.WithdrawalEvent;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

@ExtendWith(MockitoExtension.class)
class WithdrawalProcessorServiceImplTest {

    @InjectMocks WithdrawalProcessorServiceImpl underTest;

    @InjectMocks WithdrawalTypeStrategy withdrawalTypeStrategy;

    WithdrawalAsapTypeProcessor withdrawalAsapTypeProcessorMock =
            mock(WithdrawalAsapTypeProcessor.class);
    WithdrawalScheduleTypeProcessor withdrawalScheduleTypeProcessorMock =
            mock(WithdrawalScheduleTypeProcessor.class);

    VerificationMode times1 = times(1);
    @Captor ArgumentCaptor<WithdrawalEvent> withdrawalEventArgumentCaptor;

    @BeforeEach
    public void init() {
        List<WithdrawalTypeProcessor> mocks =
                List.of(withdrawalAsapTypeProcessorMock, withdrawalScheduleTypeProcessorMock);
        ReflectionTestUtils.setField(withdrawalTypeStrategy, "withdrawalTypeProcessors", mocks);
        ReflectionTestUtils.setField(underTest, "withdrawalTypeStrategy", withdrawalTypeStrategy);
    }

    @Test
    void shouldProcessWithdrawalEventAsap() {

        // arrange
        WithdrawalEvent event =
                WithdrawalEvent.builder().withdrawalType(WithdrawalType.ASAP).build();

        when(withdrawalAsapTypeProcessorMock.getType()).thenReturn(WithdrawalType.ASAP);

        // act
        underTest.process(event);

        // asserts
        verify(withdrawalAsapTypeProcessorMock, times1)
                .execute(withdrawalEventArgumentCaptor.capture());
        WithdrawalEvent eventSent = withdrawalEventArgumentCaptor.getValue();
        assertThat(eventSent).isNotNull();
    }

    @Test
    void shouldProcessWithdrawalEventScheduled() {

        // arrange
        WithdrawalEvent event =
                WithdrawalEvent.builder().withdrawalType(WithdrawalType.SCHEDULED).build();

        when(withdrawalAsapTypeProcessorMock.getType()).thenReturn(WithdrawalType.ASAP);
        when(withdrawalScheduleTypeProcessorMock.getType()).thenReturn(WithdrawalType.SCHEDULED);

        // act
        underTest.process(event);

        // asserts
        verify(withdrawalScheduleTypeProcessorMock, times1)
                .execute(withdrawalEventArgumentCaptor.capture());
        WithdrawalEvent eventSent = withdrawalEventArgumentCaptor.getValue();
        assertThat(eventSent).isNotNull();
    }
}
