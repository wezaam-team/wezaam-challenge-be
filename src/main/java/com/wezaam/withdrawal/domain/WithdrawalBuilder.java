package com.wezaam.withdrawal.domain;

import java.math.BigDecimal;
import java.time.Instant;

public class WithdrawalBuilder {
    private Long id;
    private User user;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private Boolean immediate;
    private Instant scheduledFor;
    private WithdrawalStatus withdrawalStatus;

    public static WithdrawalBuilder aWithdrawalBuilder() {
        return new WithdrawalBuilder()
                .withWithdrawalStatus(WithdrawalStatus.PENDING);
    }

    public WithdrawalBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public WithdrawalBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public WithdrawalBuilder withPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public WithdrawalBuilder withWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
        return this;
    }

    public Withdrawal build() {
        return new Withdrawal(id, user, paymentMethod, amount, scheduledFor, immediate, withdrawalStatus);
    }
}