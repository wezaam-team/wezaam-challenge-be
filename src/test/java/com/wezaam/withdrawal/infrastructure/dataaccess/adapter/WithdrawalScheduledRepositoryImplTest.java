package com.wezaam.withdrawal.infrastructure.dataaccess.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.test.util.ReflectionTestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wezaam.withdrawal.common.UtilTest;
import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.infrastructure.dataaccess.mapper.WithdrawalScheduledDataAccessMapper;
import com.wezaam.withdrawal.infrastructure.dataaccess.repository.WithdrawalScheduledJpaRepository;

@ExtendWith(MockitoExtension.class)
class WithdrawalScheduledRepositoryImplTest extends UtilTest {

    @InjectMocks WithdrawalScheduledRepositoryImpl underTest;
    @InjectMocks WithdrawalScheduledDataAccessMapper withdrawalScheduledDataAccessMapper;

    @Mock WithdrawalScheduledJpaRepository withdrawalScheduledJpaRepository;

    @Captor
    ArgumentCaptor<com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled>
            withdrawalScheduledArgumentCaptor;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(
                underTest,
                "withdrawalScheduledDataAccessMapper",
                withdrawalScheduledDataAccessMapper);
    }

    @Test
    void findByIdEmpty() {
        // arrange
        final Long withdrawalScheduledId = 1L;
        when(withdrawalScheduledJpaRepository.findById(withdrawalScheduledId))
                .thenReturn(Optional.empty());
        // act
        Optional<WithdrawalScheduled> withdrawalScheduledOptional =
                underTest.findById(withdrawalScheduledId);

        // assert
        assertThat(withdrawalScheduledOptional).isEmpty();
        verify(withdrawalScheduledJpaRepository, times1).findById(anyLong());
    }

    @Test
    void findById() {
        // arrange
        final Long withdrawalScheduledId = 1L;
        when(withdrawalScheduledJpaRepository.findById(withdrawalScheduledId))
                .thenReturn(
                        Optional.of(
                                getWithdrawalScheduled(
                                        withdrawalScheduledId, WithdrawalStatus.PENDING)));

        // act
        Optional<WithdrawalScheduled> withdrawalScheduledOptional =
                underTest.findById(withdrawalScheduledId);

        // assert
        assertThat(withdrawalScheduledOptional).isNotEmpty();
        WithdrawalScheduled withdrawalScheduled = withdrawalScheduledOptional.get();
        assertThat(withdrawalScheduled.getId()).isNotNull();
        assertThat(withdrawalScheduled.getPaymentMethodId()).isNotNull();
        assertThat(withdrawalScheduled.getStatus()).isNotNull();
        assertThat(withdrawalScheduled.getAmount()).isNotNull();
        assertThat(withdrawalScheduled.getExecuteAt()).isNotNull();
        assertThat(withdrawalScheduled.getCreatedAt()).isNotNull();

        verify(withdrawalScheduledJpaRepository, times1).findById(anyLong());
    }

    @Test
    void save() {
        // arrange
        WithdrawalScheduled withdrawalScheduled =
                WithdrawalScheduled.builder()
                        .amount(400D)
                        .executeAt(Instant.now())
                        .createdAt(Instant.now())
                        .paymentMethodId(1L)
                        .userId(1L)
                        .status(WithdrawalStatus.PENDING)
                        .build();

        when(withdrawalScheduledJpaRepository.save(
                        any(
                                com.wezaam.withdrawal.infrastructure.dataaccess.entity
                                        .WithdrawalScheduled.class)))
                .thenReturn(getWithdrawalScheduled(random.nextLong(), WithdrawalStatus.PENDING));
        // act
        WithdrawalScheduled saved = underTest.save(withdrawalScheduled);
        // assert
        assertThat(saved).isNotNull();
        verify(withdrawalScheduledJpaRepository, times1)
                .save(withdrawalScheduledArgumentCaptor.capture());
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled
                withdrawalScheduledSentToSaved = withdrawalScheduledArgumentCaptor.getValue();
        assertThat(withdrawalScheduledSentToSaved.getId()).isNull();
        assertThat(withdrawalScheduledSentToSaved.getUserId()).isNotNull();
        assertThat(withdrawalScheduledSentToSaved.getPaymentMethodId()).isNotNull();
        assertThat(withdrawalScheduledSentToSaved.getAmount()).isNotNull();
        assertThat(withdrawalScheduledSentToSaved.getCreatedAt()).isNotNull();
        assertThat(withdrawalScheduledSentToSaved.getExecuteAt()).isNotNull();
        assertThat(withdrawalScheduledSentToSaved.getTransactionId()).isNull();
        assertThat(withdrawalScheduledSentToSaved.getStatus())
                .isNotNull()
                .isEqualTo(WithdrawalStatus.PENDING);
        assertThat(withdrawalScheduledSentToSaved.getTransactionId()).isNull();
    }

    @Test
    void findAllEmpty() {
        // arrange
        when(withdrawalScheduledJpaRepository.findAll()).thenReturn(List.of());

        // act
        List<WithdrawalScheduled> list = underTest.findAll();

        // assert
        assertThat(list).isEmpty();
        verify(withdrawalScheduledJpaRepository, times1).findAll();
    }

    @Test
    void findAll() {
        // arrange
        when(withdrawalScheduledJpaRepository.findAll())
                .thenReturn(
                        List.of(
                                getWithdrawalScheduled(random.nextLong(), WithdrawalStatus.PENDING),
                                getWithdrawalScheduled(
                                        random.nextLong(), WithdrawalStatus.PROCESSING),
                                getWithdrawalScheduled(random.nextLong(), WithdrawalStatus.SUCCESS),
                                getWithdrawalScheduled(random.nextLong(), WithdrawalStatus.FAILED),
                                getWithdrawalScheduled(
                                        random.nextLong(), WithdrawalStatus.INTERNAL_ERROR)));
        // act
        List<WithdrawalScheduled> list = underTest.findAll();
        // assert
        assertThat(list).isNotEmpty().hasSize(5);
        verify(withdrawalScheduledJpaRepository, times1).findAll();
    }

    @Test
    void findAllByExecuteAtBefore() {

        // arrange
        when(withdrawalScheduledJpaRepository.findAllByExecuteAtBefore(any(Instant.class)))
                .thenReturn(
                        List.of(
                                getWithdrawalScheduled(random.nextLong(), WithdrawalStatus.PENDING),
                                getWithdrawalScheduled(random.nextLong(), WithdrawalStatus.FAILED),
                                getWithdrawalScheduled(random.nextLong(), WithdrawalStatus.SUCCESS),
                                getWithdrawalScheduled(
                                        random.nextLong(), WithdrawalStatus.INTERNAL_ERROR),
                                getWithdrawalScheduled(
                                        random.nextLong(), WithdrawalStatus.PENDING)));
        // act
        List<WithdrawalScheduled> list = underTest.findAllByExecuteAtBefore(Instant.now());

        // assert
        assertThat(list).isNotEmpty().hasSize(5);
        verify(withdrawalScheduledJpaRepository, times1)
                .findAllByExecuteAtBefore(any(Instant.class));
    }

    @Test
    void findAllByExecuteAtBeforeAndStatus() {

        // arrange
        when(withdrawalScheduledJpaRepository.findAllByExecuteAtBeforeAndStatus(
                        any(Instant.class), any(WithdrawalStatus.class)))
                .thenReturn(
                        List.of(
                                getWithdrawalScheduled(random.nextLong(), WithdrawalStatus.PENDING),
                                getWithdrawalScheduled(random.nextLong(), WithdrawalStatus.PENDING),
                                getWithdrawalScheduled(random.nextLong(), WithdrawalStatus.PENDING),
                                getWithdrawalScheduled(random.nextLong(), WithdrawalStatus.PENDING),
                                getWithdrawalScheduled(
                                        random.nextLong(), WithdrawalStatus.PENDING)));
        // act
        List<WithdrawalScheduled> list =
                underTest.findAllByExecuteAtBeforeWithStatus(
                        Instant.now(), WithdrawalStatus.PENDING);

        // assert
        assertThat(list).isNotEmpty().hasSize(5);
        assertThat(
                        list.stream()
                                .map(WithdrawalScheduled::getStatus)
                                .allMatch(WithdrawalStatus.PENDING::equals))
                .isTrue();
        verify(withdrawalScheduledJpaRepository, times1)
                .findAllByExecuteAtBeforeAndStatus(any(Instant.class), any(WithdrawalStatus.class));
    }

    protected com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled
            getWithdrawalScheduled(Long id, WithdrawalStatus status) {
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled
                withdrawalScheduled =
                        new com.wezaam.withdrawal.infrastructure.dataaccess.entity
                                .WithdrawalScheduled();
        withdrawalScheduled.setId(id);
        withdrawalScheduled.setStatus(status);
        withdrawalScheduled.setAmount(300D);
        withdrawalScheduled.setUserId(1L);
        withdrawalScheduled.setPaymentMethodId(1L);
        withdrawalScheduled.setCreatedAt(Instant.now());
        withdrawalScheduled.setExecuteAt(Instant.now().plus(10000, ChronoUnit.SECONDS));
        withdrawalScheduled.setTransactionId(
                status.equals(WithdrawalStatus.PROCESSING) ? new Random().nextLong() : null);
        return withdrawalScheduled;
    }
}
