package com.wezaam.withdrawal.rest.api.withdrawal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.wezaam.withdrawal.model.WithdrawalStatus;

import java.time.Instant;

public class ScheduledWithdrawalDto extends WithdrawalDto {

   private final Instant executeAt;

   @JsonCreator
    public ScheduledWithdrawalDto(Long id, Long transactionId, Double amount, Instant createdAt, Long userId, Long paymentMethodId, WithdrawalStatus status, Instant executeAt) {
        super(id, transactionId, amount, createdAt, userId, paymentMethodId, status);
        this.executeAt = executeAt;
    }

    public Instant getExecuteAt() {
        return executeAt;
    }
}
