package com.wezaam.withdrawal.rest.api.withdrawal;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class CreateWithdrawalDto {

    @NotNull(message = "user id can't be null")
    private final Long userId;

    @NotNull(message = "payment method id can't be null")
    private final Long paymentMethodId;

    @NotNull(message = "amount can't be null")
    private final Double amount;

    @NotNull(message = "execution type can't be null")
    private final String executionType;

    private final Instant executedAt;

    @JsonCreator
    public CreateWithdrawalDto(Long userId, Long paymentMethodId, Double amount, String executionType, Instant executedAt) {
        this.userId = userId;
        this.paymentMethodId = paymentMethodId;
        this.amount = amount;
        this.executionType = executionType;
        this.executedAt = executedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getExecutionType() {
        return executionType;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }
}
