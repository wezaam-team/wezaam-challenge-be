package com.wezaam.withdrawal.domain.model;

import com.wezaam.common.domain.model.DomainEvent;
import java.time.Instant;

public class WithdrawalCreated implements DomainEvent {
    private long withdrawalId;
    private long userId;
    private long paymentMethodId;
    private double withdrawalAmount;
    private Instant withdrawalCreatedAt;
    private Instant withdrawalExecuteAt;
    private WithdrawalStatus withdrawalStatus;
    private Instant occurredOn;

    protected WithdrawalCreated() {}

    public WithdrawalCreated(
            long withdrawalId,
            long userId,
            long paymentMethodId,
            double withdrawalAmount,
            Instant withdrawalCreatedAt,
            Instant withdrawalExecuteAt,
            WithdrawalStatus withdrawalStatus) {
        this.setWithdrawalId(withdrawalId);
        this.setUserId(userId);
        this.setPaymentMethodId(paymentMethodId);
        this.setWithdrawalAmount(withdrawalAmount);
        this.setWithdrawalCreatedAt(withdrawalCreatedAt);
        this.setWithdrawalExecuteAt(withdrawalExecuteAt);
        this.setWithdrawalStatus(withdrawalStatus);
        this.setOccurredOn(Instant.now());
    }

    protected void setWithdrawalId(long withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public long getWithdrawalId() {
        return this.withdrawalId;
    }

    protected void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return this.userId;
    }

    protected void setPaymentMethodId(long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public long getPaymentMethodId() {
        return this.paymentMethodId;
    }

    protected void setWithdrawalAmount(double withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public double getWithdrawalAmount() {
        return this.withdrawalAmount;
    }

    protected void setWithdrawalCreatedAt(Instant withdrawalCreatedAt) {
        this.withdrawalCreatedAt = withdrawalCreatedAt;
    }

    public Instant getWithdrawalCreatedAt() {
        return this.withdrawalCreatedAt;
    }

    protected void setWithdrawalExecuteAt(Instant withdrawalExecuteAt) {
        this.withdrawalExecuteAt = withdrawalExecuteAt;
    }

    public Instant getWithdrawalExecuteAt() {
        return this.withdrawalExecuteAt;
    }

    protected void setWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }

    public WithdrawalStatus getWithdrawalStatus() {
        return this.withdrawalStatus;
    }

    protected void setOccurredOn(Instant occurredOn) {
        if (occurredOn == null) {
            throw new IllegalArgumentException("The occurredOn should not be null.");
        }

        this.occurredOn = occurredOn;
    }

    @Override
    public Instant getOccurredOn() {
        return this.occurredOn;
    }

    @Override
    public String toString() {
        return "WithdrawalCreated{"
                + "withdrawalId=" + this.getWithdrawalId()
                + ", userId=" + this.getUserId()
                + ", paymentMethodId=" + this.getPaymentMethodId()
                + ", withdrawalAmount=" + this.getWithdrawalAmount()
                + ", withdrawalCreatedAt=" + this.getWithdrawalCreatedAt()
                + ", withdrawalExecuteAt=" + this.getWithdrawalExecuteAt()
                + ", withdrawalStatus=" + this.getWithdrawalStatus()
                + ", occurredOn=" + this.getOccurredOn()
                + '}';
    }
}
