package com.wezaam.withdrawal.domain.event;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class WithdrawalCreatedBuilder {
    private Long id;
    private Long userId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private Boolean immediate;
    private Instant scheduledFor;
    private WithdrawalStatus withdrawalStatus;

    public static WithdrawalCreatedBuilder aWithdrawalCreatedBuilder() {
        return new WithdrawalCreatedBuilder();
    }

    public WithdrawalCreatedBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public WithdrawalCreatedBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public WithdrawalCreatedBuilder withPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public WithdrawalCreatedBuilder withImmediate(Boolean immediate) {
        this.immediate = immediate;
        return this;
    }

    public WithdrawalCreatedBuilder withScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public WithdrawalCreatedBuilder withWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
        return this;
    }

    public WithdrawalCreatedBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public WithdrawalCreated build() {
        return new WithdrawalCreated(id, userId, paymentMethodId, amount,immediate,scheduledFor,withdrawalStatus);
    }
}