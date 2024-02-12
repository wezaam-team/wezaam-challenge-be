package com.wezaam.withdrawal.application.command;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class ProcessWithdrawalCommandBuilder {
    private Long id;
    private Long userId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private Boolean immediate;
    private Instant scheduledFor;
    private WithdrawalStatus withdrawalStatus;

    public static ProcessWithdrawalCommandBuilder aProcessWithdrawalCommandBuilder() {
        return new ProcessWithdrawalCommandBuilder();
    }

    public ProcessWithdrawalCommandBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProcessWithdrawalCommandBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public ProcessWithdrawalCommandBuilder withPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public ProcessWithdrawalCommandBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ProcessWithdrawalCommandBuilder withImmediate(Boolean immediate) {
        this.immediate = immediate;
        return this;
    }

    public ProcessWithdrawalCommandBuilder withScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public ProcessWithdrawalCommandBuilder withWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
        return this;
    }

    public ProcessWithdrawalCommand build() {
        return new ProcessWithdrawalCommand(id, userId, paymentMethodId, amount, immediate, scheduledFor, withdrawalStatus);
    }
}
