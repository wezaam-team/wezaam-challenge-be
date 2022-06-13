package com.wezaam.withdrawal.rest.api.withdrawal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.wezaam.withdrawal.model.WithdrawalStatus;

import java.time.Instant;

public class WithdrawalDto {
    private final Long id;
    private final Long transactionId;
    private final Double amount;
    private final Instant createdAt;
    private final Long userId;
    private final Long paymentMethodId;
    private final WithdrawalStatus status;

    @JsonCreator
    public WithdrawalDto(Long id, Long transactionId, Double amount, Instant createdAt, Long userId, Long paymentMethodId, WithdrawalStatus status) {
        this.id = id;
        this.transactionId = transactionId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.userId = userId;
        this.paymentMethodId = paymentMethodId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }
}
