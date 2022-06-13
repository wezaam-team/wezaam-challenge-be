package com.wezaam.withdrawal.mapper;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalStatus;
import com.wezaam.withdrawal.rest.api.withdrawal.WithdrawalDto;
import com.wezaam.withdrawal.rest.mapper.WithdrawalMapper;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.wezaam.withdrawal.model.WithdrawalStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;

public class WithdrawalMapperTest {

    @Test
    void shouldMapAllFieldsToWithdrawalDto(){
        Instant instant = LocalDateTime.of(2022,6,13,23,30).toInstant(ZoneOffset.UTC);
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(10d);
        withdrawal.setPaymentMethodId(1L);
        withdrawal.setId(2L);
        withdrawal.setStatus(PENDING);
        withdrawal.setUserId(3L);
        withdrawal.setTransactionId(4L);
        withdrawal.setCreatedAt(instant);

        WithdrawalDto dto = WithdrawalMapper.toDto.apply(withdrawal);

        assertThat(dto)
                .extracting("id", "transactionId", "amount", "createdAt", "userId", "paymentMethodId", "status")
                .containsExactlyInAnyOrder(2L, 4L, 10d, instant, 3L, 1L, PENDING);
    }
}
