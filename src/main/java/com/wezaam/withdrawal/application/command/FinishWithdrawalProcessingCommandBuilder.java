package com.wezaam.withdrawal.application.command;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class FinishWithdrawalProcessingCommandBuilder {
    private Long id;
    private Long userId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private Boolean immediate;
    private Instant scheduledFor;
    private WithdrawalStatus withdrawalStatus;

    public static FinishWithdrawalProcessingCommandBuilder aFinishWithdrawalProcessingCommandBuilder() {
        return new FinishWithdrawalProcessingCommandBuilder();
    }

    public FinishWithdrawalProcessingCommandBuilder withId(Long id) {
        this.id = id;
        return this;
    }


    public FinishWithdrawalProcessingCommandBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public FinishWithdrawalProcessingCommandBuilder withPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public FinishWithdrawalProcessingCommandBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public FinishWithdrawalProcessingCommandBuilder withImmediate(Boolean immediate) {
        this.immediate = immediate;
        return this;
    }

    public FinishWithdrawalProcessingCommandBuilder withScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public FinishWithdrawalProcessingCommandBuilder withWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
        return this;
    }

    public FinishWithdrawalProcessingCommand build() {
        return new FinishWithdrawalProcessingCommand(id, userId, paymentMethodId, amount, immediate, scheduledFor, withdrawalStatus);
    }
}
