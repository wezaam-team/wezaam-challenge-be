package com.wezaam.withdrawal.acceptance.dto.builder;

import com.wezaam.withdrawal.acceptance.dto.Withdrawal;

import java.math.BigDecimal;
import java.time.Instant;

public class WithdrawalBuilder {

    private Long userId;

    private Long paymentMethodId;

    private BigDecimal amount;

    private Boolean immediate;

    private Instant scheduledFor;

    private WithdrawalBuilder() {
        super();
    }

    public static WithdrawalBuilder aWithdrawalBuilder() {
        return new WithdrawalBuilder();
    }

    public WithdrawalBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public WithdrawalBuilder withPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public WithdrawalBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public WithdrawalBuilder withImmediate(Boolean immediate) {
        this.immediate = immediate;
        return this;
    }

    public WithdrawalBuilder withScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public Withdrawal build() {
        return new Withdrawal(userId, paymentMethodId, amount, immediate, scheduledFor);
    }
}