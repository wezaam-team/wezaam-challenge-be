package com.wezaam.withdrawal.domain.event;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class WithdrawalProcessedBuilder {
    private Long id;
    private Long userId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private Boolean immediate;
    private Instant scheduledFor;
    private WithdrawalStatus withdrawalStatus;

    public static WithdrawalProcessedBuilder aWithdrawalProcessedBuilder() {
        return new WithdrawalProcessedBuilder();
    }

    public WithdrawalProcessedBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public WithdrawalProcessedBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public WithdrawalProcessedBuilder withPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public WithdrawalProcessedBuilder withImmediate(Boolean immediate) {
        this.immediate = immediate;
        return this;
    }

    public WithdrawalProcessedBuilder withScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public WithdrawalProcessedBuilder withWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
        return this;
    }

    public WithdrawalProcessedBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public WithdrawalProcessed build() {
        return new WithdrawalProcessed(id, userId, paymentMethodId, amount,immediate,scheduledFor,withdrawalStatus);
    }
}