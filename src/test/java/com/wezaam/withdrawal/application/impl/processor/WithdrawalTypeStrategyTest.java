package com.wezaam.withdrawal.application.impl.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wezaam.withdrawal.domain.exception.NotFoundStrategyProcessorException;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

@ExtendWith(MockitoExtension.class)
class WithdrawalTypeStrategyTest {

    @InjectMocks WithdrawalTypeStrategy underTest;

    WithdrawalAsapTypeProcessor withdrawalAsapTypeProcessorMock =
            mock(WithdrawalAsapTypeProcessor.class);

    WithdrawalScheduleTypeProcessor withdrawalScheduleTypeProcessorMock =
            mock(WithdrawalScheduleTypeProcessor.class);

    @Test
    void shouldGetStrategyAsap() {

        // arrange
        List<WithdrawalTypeProcessor> list = List.of(withdrawalAsapTypeProcessorMock);
        ReflectionTestUtils.setField(underTest, "withdrawalTypeProcessors", list);
        when(withdrawalAsapTypeProcessorMock.getType()).thenReturn(WithdrawalType.ASAP);

        // act
        WithdrawalTypeProcessor withdrawalProcessor =
                underTest.getWithdrawalProcessor(WithdrawalType.ASAP);

        // asserts
        assertThat(withdrawalProcessor).isNotNull();
    }

    @Test
    void shouldGetStrategyScheduled() {

        // arrange
        List<WithdrawalTypeProcessor> list = List.of(withdrawalScheduleTypeProcessorMock);
        ReflectionTestUtils.setField(underTest, "withdrawalTypeProcessors", list);
        when(withdrawalScheduleTypeProcessorMock.getType()).thenReturn(WithdrawalType.SCHEDULED);

        // act
        WithdrawalTypeProcessor withdrawalProcessor =
                underTest.getWithdrawalProcessor(WithdrawalType.SCHEDULED);

        // asserts
        assertThat(withdrawalProcessor).isNotNull();
    }

    @Test
    void shouldGetStrategyNotFoundStrategy() {

        // arrange
        List<WithdrawalTypeProcessor> list =
                List.of(withdrawalScheduleTypeProcessorMock, withdrawalAsapTypeProcessorMock);
        ReflectionTestUtils.setField(underTest, "withdrawalTypeProcessors", list);

        when(withdrawalScheduleTypeProcessorMock.getType()).thenReturn(WithdrawalType.SCHEDULED);
        when(withdrawalAsapTypeProcessorMock.getType()).thenReturn(WithdrawalType.ASAP);

        // act
        NotFoundStrategyProcessorException exception =
                assertThrows(
                        NotFoundStrategyProcessorException.class,
                        () -> underTest.getWithdrawalProcessor(WithdrawalType.TEST));

        // asserts
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage())
                .isNotNull()
                .isEqualTo("There is not implementation for that processor: TEST");
    }
}
