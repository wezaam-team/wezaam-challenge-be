package com.wezaam.withdrawal.application.rest.dto;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class CreateWithdrawalResponseBuilder {
    private Long id;
    private Long userId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private Boolean immediate;
    private Instant scheduledFor;
    private WithdrawalStatus withdrawalStatus;

    public static CreateWithdrawalResponseBuilder aCreateWithdrawalResponseBuilder() {
        return new CreateWithdrawalResponseBuilder();
    }

    public CreateWithdrawalResponseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CreateWithdrawalResponseBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public CreateWithdrawalResponseBuilder withPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public CreateWithdrawalResponseBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public CreateWithdrawalResponseBuilder withImmediate(Boolean immediate) {
        this.immediate = immediate;
        return this;
    }

    public CreateWithdrawalResponseBuilder withScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public CreateWithdrawalResponseBuilder withWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
        return this;
    }

    public CreateWithdrawalResponse build() {
        return new CreateWithdrawalResponse(id, userId, paymentMethodId, amount, immediate, scheduledFor, withdrawalStatus);
    }
}