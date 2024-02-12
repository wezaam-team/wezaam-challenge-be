package com.wezaam.withdrawal.domain.event;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class WithdrawalInvalidatedBuilder {
    private Long id;
    private Long userId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private Boolean immediate;
    private Instant scheduledFor;
    private WithdrawalStatus withdrawalStatus;

    public static WithdrawalInvalidatedBuilder aWithdrawalInvalidatedBuilder() {
        return new WithdrawalInvalidatedBuilder();
    }

    public WithdrawalInvalidatedBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public WithdrawalInvalidatedBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public WithdrawalInvalidatedBuilder withPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public WithdrawalInvalidatedBuilder withImmediate(Boolean immediate) {
        this.immediate = immediate;
        return this;
    }

    public WithdrawalInvalidatedBuilder withScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public WithdrawalInvalidatedBuilder withWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
        return this;
    }

    public WithdrawalInvalidatedBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public WithdrawalInvalidated build() {
        return new WithdrawalInvalidated(id, userId, paymentMethodId, amount,immediate,scheduledFor,withdrawalStatus);
    }
}
