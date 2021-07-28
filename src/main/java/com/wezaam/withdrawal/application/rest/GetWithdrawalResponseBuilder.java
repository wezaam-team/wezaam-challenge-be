package com.wezaam.withdrawal.application.rest;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class GetWithdrawalResponseBuilder {
    private Long id;
    private Long userId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private Boolean immediate;
    private Instant scheduledFor;
    private WithdrawalStatus withdrawalStatus;

    public static GetWithdrawalResponseBuilder aGetWithdrawalResponseBuilder() {
        return new GetWithdrawalResponseBuilder();
    }

    public GetWithdrawalResponseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public GetWithdrawalResponseBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public GetWithdrawalResponseBuilder withPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public GetWithdrawalResponseBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public GetWithdrawalResponseBuilder withImmediate(Boolean immediate) {
        this.immediate = immediate;
        return this;
    }

    public GetWithdrawalResponseBuilder withScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public GetWithdrawalResponseBuilder withWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
        return this;
    }

    public GetWithdrawalResponse build() {
        return new GetWithdrawalResponse(id, userId, paymentMethodId, amount, immediate, scheduledFor, withdrawalStatus);
    }
}