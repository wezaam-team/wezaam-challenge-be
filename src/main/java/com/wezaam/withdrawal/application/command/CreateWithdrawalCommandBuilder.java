package com.wezaam.withdrawal.application.command;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class CreateWithdrawalCommandBuilder {
    private Long id;
    private Long userId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private Boolean immediate;
    private Instant scheduledFor;
    private WithdrawalStatus withdrawalStatus;

    public static CreateWithdrawalCommandBuilder aCreateWithdrawalCommandBuilder() {
        return new CreateWithdrawalCommandBuilder();
    }

    public CreateWithdrawalCommandBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CreateWithdrawalCommandBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public CreateWithdrawalCommandBuilder withPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public CreateWithdrawalCommandBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public CreateWithdrawalCommandBuilder withImmediate(Boolean immediate) {
        this.immediate = immediate;
        return this;
    }

    public CreateWithdrawalCommandBuilder withScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public CreateWithdrawalCommandBuilder withWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
        return this;
    }

    public CreateWithdrawalCommand build() {
        return new CreateWithdrawalCommand(id, userId, paymentMethodId, amount, immediate, scheduledFor, withdrawalStatus);
    }
}