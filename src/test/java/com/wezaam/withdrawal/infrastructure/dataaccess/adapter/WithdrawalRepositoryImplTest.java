package com.wezaam.withdrawal.infrastructure.dataaccess.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
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
import com.wezaam.withdrawal.domain.entity.Withdrawal;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.infrastructure.dataaccess.mapper.WithdrawalDataAccessMapper;
import com.wezaam.withdrawal.infrastructure.dataaccess.repository.WithdrawalJpaRepository;

@ExtendWith(MockitoExtension.class)
class WithdrawalRepositoryImplTest extends UtilTest {

    @InjectMocks WithdrawalRepositoryImpl underTest;
    @InjectMocks WithdrawalDataAccessMapper withdrawalDataAccessMapper;

    @Mock WithdrawalJpaRepository withdrawalJpaRepository;

    @Captor
    ArgumentCaptor<com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal>
            withdrawalArgumentCaptor;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(
                underTest, "withdrawalDataAccessMapper", withdrawalDataAccessMapper);
    }

    @Test
    void findByIdEmpty() {
        // arrange
        final Long withdrawalId = 1L;
        when(withdrawalJpaRepository.findById(withdrawalId)).thenReturn(Optional.empty());

        // act
        Optional<Withdrawal> withdrawalOptional = underTest.findById(withdrawalId);

        // assert
        assertThat(withdrawalOptional).isEmpty();
        verify(withdrawalJpaRepository, times1).findById(anyLong());
    }

    @Test
    void findByIdPendingStatus() {

        // arrange
        final Long withdrawalId = 1L;
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal withdrawal =
                getWithdrawal();
        when(withdrawalJpaRepository.findById(withdrawalId)).thenReturn(Optional.of(withdrawal));

        // act
        Optional<Withdrawal> withdrawalOptional = underTest.findById(withdrawalId);

        // assert
        assertThat(withdrawalOptional).isNotEmpty();
        Withdrawal withdrawalResponse = withdrawalOptional.get();
        assertThat(withdrawalResponse).isNotNull();
        assertThat(withdrawalResponse.getId()).isNotNull();
        assertThat(withdrawalResponse.getUserId()).isNotNull();
        assertThat(withdrawalResponse.getPaymentMethodId()).isNotNull();
        assertThat(withdrawalResponse.getAmount()).isNotNull();
        assertThat(withdrawalResponse.getStatus()).isNotNull().isEqualTo(WithdrawalStatus.PENDING);
        assertThat(withdrawalResponse.getCreatedAt()).isNotNull();
        assertThat(withdrawalResponse.getTransactionId()).isNull();

        verify(withdrawalJpaRepository, times1).findById(anyLong());
    }

    @Test
    void findByIdProcessingStatus() {

        // arrange
        final Long withdrawalId = 1L;
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal withdrawal =
                getWithdrawal();
        withdrawal.setTransactionId(new Random().nextLong());
        withdrawal.setStatus(WithdrawalStatus.PROCESSING);

        when(withdrawalJpaRepository.findById(withdrawalId)).thenReturn(Optional.of(withdrawal));

        // act
        Optional<Withdrawal> withdrawalOptional = underTest.findById(withdrawalId);

        // assert
        assertThat(withdrawalOptional).isNotEmpty();
        Withdrawal withdrawalResponse = withdrawalOptional.get();
        assertThat(withdrawalResponse).isNotNull();
        assertThat(withdrawalResponse.getId()).isNotNull();
        assertThat(withdrawalResponse.getUserId()).isNotNull();
        assertThat(withdrawalResponse.getPaymentMethodId()).isNotNull();
        assertThat(withdrawalResponse.getAmount()).isNotNull();
        assertThat(withdrawalResponse.getStatus())
                .isNotNull()
                .isEqualTo(WithdrawalStatus.PROCESSING);
        assertThat(withdrawalResponse.getCreatedAt()).isNotNull();
        assertThat(withdrawalResponse.getTransactionId()).isNotNull();

        verify(withdrawalJpaRepository, times1).findById(anyLong());
    }

    @Test
    void save() {
        // arrange
        Withdrawal withdrawal =
                Withdrawal.builder()
                        .userId(1L)
                        .amount(300D)
                        .paymentMethodId(1L)
                        .status(WithdrawalStatus.PENDING)
                        .createdAt(Instant.now())
                        .build();
        when(withdrawalJpaRepository.save(
                        any(
                                com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal
                                        .class)))
                .thenReturn(getWithdrawal(random.nextLong(), WithdrawalStatus.PENDING));

        // act
        Withdrawal withdrawalResponse = underTest.save(withdrawal);

        // assert
        assertThat(withdrawalResponse).isNotNull();
        assertThat(withdrawalResponse.getId()).isNotNull();
        assertThat(withdrawalResponse.getUserId()).isNotNull();
        assertThat(withdrawalResponse.getPaymentMethodId()).isNotNull();
        assertThat(withdrawalResponse.getAmount()).isNotNull();
        assertThat(withdrawalResponse.getCreatedAt()).isNotNull();
        assertThat(withdrawalResponse.getTransactionId()).isNull();
        assertThat(withdrawalResponse.getStatus()).isNotNull().isEqualTo(WithdrawalStatus.PENDING);
        verify(withdrawalJpaRepository, times1).save(withdrawalArgumentCaptor.capture());
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal withdrawalSentToSaved =
                withdrawalArgumentCaptor.getValue();
        assertThat(withdrawalSentToSaved.getId()).isNull();
        assertThat(withdrawalSentToSaved.getUserId()).isNotNull();
        assertThat(withdrawalSentToSaved.getPaymentMethodId()).isNotNull();
        assertThat(withdrawalSentToSaved.getAmount()).isNotNull();
        assertThat(withdrawalSentToSaved.getCreatedAt()).isNotNull();
        assertThat(withdrawalSentToSaved.getTransactionId()).isNull();
        assertThat(withdrawalSentToSaved.getStatus())
                .isNotNull()
                .isEqualTo(WithdrawalStatus.PENDING);
        assertThat(withdrawalSentToSaved.getTransactionId()).isNull();
    }

    @Test
    void findAll() {

        // arrange
        when(withdrawalJpaRepository.findAll())
                .thenReturn(
                        List.of(
                                getWithdrawal(random.nextLong(), WithdrawalStatus.PENDING),
                                getWithdrawal(random.nextLong(), WithdrawalStatus.PROCESSING),
                                getWithdrawal(random.nextLong(), WithdrawalStatus.FAILED),
                                getWithdrawal(random.nextLong(), WithdrawalStatus.SUCCESS),
                                getWithdrawal(random.nextLong(), WithdrawalStatus.INTERNAL_ERROR)));

        // act
        List<Withdrawal> list = underTest.findAll();

        // assert
        assertThat(list).isNotEmpty().hasSize(5);
        verify(withdrawalJpaRepository, times1).findAll();
    }

    protected com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal getWithdrawal() {
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal withdrawal =
                new com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal();
        withdrawal.setId(1L);
        withdrawal.setStatus(WithdrawalStatus.PENDING);
        withdrawal.setAmount(300D);
        withdrawal.setUserId(1L);
        withdrawal.setPaymentMethodId(1L);
        withdrawal.setCreatedAt(Instant.now());
        return withdrawal;
    }

    protected com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal getWithdrawal(
            Long id, WithdrawalStatus status) {
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal withdrawal =
                new com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal();
        withdrawal.setId(id);
        withdrawal.setStatus(status);
        withdrawal.setAmount(300D);
        withdrawal.setUserId(1L);
        withdrawal.setPaymentMethodId(1L);
        withdrawal.setCreatedAt(Instant.now());
        withdrawal.setTransactionId(
                status.equals(WithdrawalStatus.PROCESSING) ? new Random().nextLong() : null);
        return withdrawal;
    }
}
