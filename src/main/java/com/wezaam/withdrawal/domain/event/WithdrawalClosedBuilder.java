package com.wezaam.withdrawal.domain.event;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class WithdrawalClosedBuilder {
    private Long id;
    private Long userId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private Boolean immediate;
    private Instant scheduledFor;
    private WithdrawalStatus withdrawalStatus;

    public static WithdrawalClosedBuilder aWithdrawalClosedBuilder() {
        return new WithdrawalClosedBuilder();
    }

    public WithdrawalClosedBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public WithdrawalClosedBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public WithdrawalClosedBuilder withPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public WithdrawalClosedBuilder withImmediate(Boolean immediate) {
        this.immediate = immediate;
        return this;
    }

    public WithdrawalClosedBuilder withScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public WithdrawalClosedBuilder withWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
        return this;
    }

    public WithdrawalClosedBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public WithdrawalClosed build() {
        return new WithdrawalClosed(id, userId, paymentMethodId, amount,immediate,scheduledFor,withdrawalStatus);
    }
}
