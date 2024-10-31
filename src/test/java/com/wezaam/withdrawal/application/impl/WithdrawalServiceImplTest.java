package com.wezaam.withdrawal.application.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wezaam.withdrawal.application.WithdrawalProcessorService;
import com.wezaam.withdrawal.common.UtilTest;
import com.wezaam.withdrawal.domain.command.CreateWithdrawalCommand;
import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.domain.entity.User;
import com.wezaam.withdrawal.domain.entity.Withdrawal;
import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.event.WithdrawalEvent;
import com.wezaam.withdrawal.domain.exception.NotFoundException;
import com.wezaam.withdrawal.domain.exception.WithdrawalDomainException;
import com.wezaam.withdrawal.domain.ports.output.repository.UserRepository;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalRepository;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

@ExtendWith(MockitoExtension.class)
class WithdrawalServiceImplTest extends UtilTest {

    @InjectMocks WithdrawalServiceImpl underTest;

    @Mock WithdrawalRepository withdrawalRepository;

    @Mock WithdrawalScheduledRepository withdrawalScheduledRepository;

    @Mock UserRepository userRepository;

    @Mock WithdrawalProcessorService withdrawalProcessorService;

    @Captor ArgumentCaptor<WithdrawalEvent> withdrawalEventArgumentCaptor;

    @Captor ArgumentCaptor<Withdrawal> withdrawalArgumentCaptor;

    @Captor ArgumentCaptor<WithdrawalScheduled> withdrawalScheduledArgumentCaptor;

    @BeforeEach
    public void init() {}

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldCreateWithdrawalsUserNotFound(int source) {

        // arrange
        final long userId = 1;
        CreateWithdrawalCommand request =
                source == 1
                        ? CreateWithdrawalCommand.builder()
                                .userId(1L)
                                .paymentMethodId(1L)
                                .type(WithdrawalType.ASAP)
                                .amount(400D)
                                .createAt(Instant.now())
                                .build()
                        : CreateWithdrawalCommand.builder()
                                .userId(1L)
                                .paymentMethodId(1L)
                                .amount(400.0D)
                                .createAt(Instant.now())
                                .executeAt(Instant.parse("2024-11-30T18:35:24.00Z"))
                                .type(WithdrawalType.SCHEDULED)
                                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // act
        NotFoundException notFoundException =
                assertThrows(NotFoundException.class, () -> underTest.create(request));

        // assert
        assertThat(notFoundException).isNotNull();
        assertThat(notFoundException.getMessage()).isNotNull().isEqualTo("User not found");
        verify(userRepository, times1).findById(anyLong());
    }

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldCreateWithdrawalsWithdrawalDomainExceptionMaxWithdrawalAllowed(int source) {

        // arrange
        final long userId = 1;
        CreateWithdrawalCommand request =
                source == 1
                        ? CreateWithdrawalCommand.builder()
                                .userId(1L)
                                .paymentMethodId(1L)
                                .type(WithdrawalType.ASAP)
                                .amount(400.0D)
                                .build()
                        : CreateWithdrawalCommand.builder()
                                .userId(1L)
                                .paymentMethodId(1L)
                                .amount(400.0D)
                                .type(WithdrawalType.SCHEDULED)
                                .executeAt(Instant.parse("2024-11-30T18:35:24.00Z"))
                                .build();
        when(userRepository.findById(userId))
                .thenReturn(
                        Optional.of(
                                com.wezaam.withdrawal.domain.entity.User.builder()
                                        .maxWithdrawalAmount(350D)
                                        .build()));

        // act
        WithdrawalDomainException exception =
                assertThrows(WithdrawalDomainException.class, () -> underTest.create(request));

        // assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage())
                .isNotNull()
                .isEqualTo("Withdrawal amount exceeds the maximum allowed limit of 350.0");
        verify(userRepository, times1).findById(anyLong());
    }

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldCreateWithdrawalsWithdrawalDomainExceptionPaymentMethodEmpty(int source) {

        // arrange
        final long userId = 1;
        CreateWithdrawalCommand request =
                source == 1
                        ? CreateWithdrawalCommand.builder()
                                .userId(1L)
                                .paymentMethodId(1L)
                                .type(WithdrawalType.ASAP)
                                .amount(400.0D)
                                .build()
                        : CreateWithdrawalCommand.builder()
                                .userId(1L)
                                .paymentMethodId(1L)
                                .amount(400.0D)
                                .type(WithdrawalType.SCHEDULED)
                                .executeAt(Instant.parse("2024-11-30T18:35:24.00Z"))
                                .build();
        when(userRepository.findById(userId))
                .thenReturn(
                        Optional.of(
                                com.wezaam.withdrawal.domain.entity.User.builder()
                                        .paymentMethods(List.of())
                                        .maxWithdrawalAmount(450D)
                                        .build()));

        // act
        WithdrawalDomainException exception =
                assertThrows(WithdrawalDomainException.class, () -> underTest.create(request));

        // assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage())
                .isNotNull()
                .isEqualTo("Invalid payment method: No valid payment methods provided.");
        verify(userRepository, times1).findById(anyLong());
    }

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldCreateWithdrawalsWithdrawalDomainExceptionPaymentMethodNotMatch(int source) {

        // arrange
        final long userId = 1;
        CreateWithdrawalCommand request =
                source == 1
                        ? CreateWithdrawalCommand.builder()
                                .userId(1L)
                                .paymentMethodId(1L)
                                .type(WithdrawalType.ASAP)
                                .amount(400.0D)
                                .build()
                        : CreateWithdrawalCommand.builder()
                                .userId(1L)
                                .paymentMethodId(1L)
                                .amount(400.0D)
                                .type(WithdrawalType.SCHEDULED)
                                .executeAt(Instant.parse("2024-11-30T18:35:24.00Z"))
                                .build();
        when(userRepository.findById(userId))
                .thenReturn(
                        Optional.of(
                                com.wezaam.withdrawal.domain.entity.User.builder()
                                        .paymentMethods(
                                                List.of(
                                                        PaymentMethod.builder()
                                                                .id(2L)
                                                                .name("Bank account")
                                                                .build()))
                                        .maxWithdrawalAmount(450D)
                                        .build()));

        // act
        WithdrawalDomainException exception =
                assertThrows(WithdrawalDomainException.class, () -> underTest.create(request));

        // assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage())
                .isNotNull()
                .isEqualTo("Invalid payment method: Not among allowed payment methods.");
        verify(userRepository, times1).findById(anyLong());
    }

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldCreateWithdrawals(int source) {

        // arrange
        final long userId = 1;
        CreateWithdrawalCommand request =
                source == 1
                        ? CreateWithdrawalCommand.builder()
                                .userId(1L)
                                .paymentMethodId(1L)
                                .type(WithdrawalType.ASAP)
                                .createAt(Instant.now())
                                .amount(400.0D)
                                .status(WithdrawalStatus.PENDING)
                                .build()
                        : CreateWithdrawalCommand.builder()
                                .userId(1L)
                                .paymentMethodId(1L)
                                .amount(400.0D)
                                .createAt(Instant.now())
                                .status(WithdrawalStatus.PENDING)
                                .type(WithdrawalType.SCHEDULED)
                                .executeAt(Instant.parse("2024-11-30T18:35:24.00Z"))
                                .build();
        when(userRepository.findById(userId))
                .thenReturn(
                        Optional.of(
                                User.builder()
                                        .paymentMethods(
                                                List.of(
                                                        PaymentMethod.builder()
                                                                .id(1L)
                                                                .name("Bank account")
                                                                .build()))
                                        .maxWithdrawalAmount(600D)
                                        .build()));

        if (source == 1) {
            Withdrawal withdrawal = getWithdrawal();
            when(withdrawalRepository.save(any(Withdrawal.class))).thenReturn(withdrawal);
        }
        if (source == 2) {
            WithdrawalScheduled withdrawalScheduled = getWithdrawalScheduled();
            when(withdrawalScheduledRepository.save(any(WithdrawalScheduled.class)))
                    .thenReturn(withdrawalScheduled);
        }

        // act
        Withdrawal withdrawalResponse = underTest.create(request);

        // assert
        assertThat(withdrawalResponse).isNotNull();

        verify(userRepository, times1).findById(anyLong());
        if (source == 1) {
            verify(withdrawalRepository, times1).save(withdrawalArgumentCaptor.capture());
            Withdrawal withdrawalSaved = withdrawalArgumentCaptor.getValue();
            assertThat(withdrawalSaved).isNotNull();
            assertThat(withdrawalSaved.getCreatedAt()).isNotNull();
            assertThat(withdrawalSaved.getStatus()).isNotNull();
            assertThat(withdrawalSaved.getPaymentMethodId()).isNotNull();
            assertThat(withdrawalSaved.getAmount()).isNotNull();
            assertThat(withdrawalSaved.getUserId()).isNotNull();

            verify(withdrawalProcessorService, times1)
                    .process(withdrawalEventArgumentCaptor.capture());
            WithdrawalEvent event = withdrawalEventArgumentCaptor.getValue();
            assertThat(event.getId()).isNotNull();
        }
        if (source == 2) {
            verify(withdrawalScheduledRepository, times1)
                    .save(withdrawalScheduledArgumentCaptor.capture());
            WithdrawalScheduled withdrawalScheduledSaved =
                    withdrawalScheduledArgumentCaptor.getValue();
            assertThat(withdrawalScheduledSaved).isNotNull();
            assertThat(withdrawalScheduledSaved.getAmount()).isNotNull();
            assertThat(withdrawalScheduledSaved.getCreatedAt()).isNotNull();
            assertThat(withdrawalScheduledSaved.getStatus()).isNotNull();
            assertThat(withdrawalScheduledSaved.getUserId()).isNotNull();
            assertThat(withdrawalScheduledSaved.getExecuteAt()).isNotNull();
            assertThat(withdrawalScheduledSaved.getPaymentMethodId()).isNotNull();
        }
    }

    @Test
    void testGetAllWithdrawalExceptionHandling() {
        // Arrange
        when(withdrawalRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        List<Withdrawal> result = underTest.getAllWithdrawal();

        // Assert
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void shouldGetAllWithdrawalsEmpty() {

        // arrange
        when(withdrawalRepository.findAll()).thenReturn(List.of());
        when(withdrawalScheduledRepository.findAll()).thenReturn(List.of());

        // act
        var list = underTest.getAllWithdrawal();

        // asserts
        assertThat(list).isEmpty();
    }

    @Test
    void shouldGetAllWithdrawalsOnlyWithdrawal() {

        // arrange
        when(withdrawalRepository.findAll()).thenReturn(List.of(getWithdrawal()));
        when(withdrawalScheduledRepository.findAll()).thenReturn(List.of());

        // act
        var list = underTest.getAllWithdrawal();

        // asserts
        assertThat(list).hasSize(1);
    }

    @Test
    void shouldGetAllWithdrawalsOnlyWithdrawalScheduled() {

        // arrange
        when(withdrawalRepository.findAll()).thenReturn(List.of());
        when(withdrawalScheduledRepository.findAll()).thenReturn(List.of(getWithdrawalScheduled()));

        // act
        var list = underTest.getAllWithdrawal();

        // asserts
        assertThat(list).hasSize(1);
    }

    @Test
    void shouldGetAllWithdrawals() {

        // arrange
        when(withdrawalRepository.findAll()).thenReturn(List.of(getWithdrawal()));
        when(withdrawalScheduledRepository.findAll()).thenReturn(List.of(getWithdrawalScheduled()));

        // act
        var list = underTest.getAllWithdrawal();

        // asserts
        assertThat(list).hasSize(2);
    }

    protected Withdrawal getWithdrawal() {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(1L);
        withdrawal.setStatus(WithdrawalStatus.PENDING);
        withdrawal.setAmount(300D);
        withdrawal.setUserId(1L);
        withdrawal.setPaymentMethodId(1L);
        withdrawal.setCreatedAt(Instant.now());

        return withdrawal;
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
