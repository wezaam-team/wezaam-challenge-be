package com.wezaam.withdrawal.domain.model;

import com.wezaam.common.domain.model.DomainEvent;
import java.time.Instant;

public class WithdrawalProcessingTriggered implements DomainEvent {
    private long withdrawalId;
    private long transactionId;
    private WithdrawalStatus withdrawalStatus;
    private Instant occurredOn;

    protected WithdrawalProcessingTriggered() {}

    public WithdrawalProcessingTriggered(long withdrawalId, long transactionId, WithdrawalStatus withdrawalStatus) {
        this.setWithdrawalId(withdrawalId);
        this.setTransactionId(transactionId);
        this.setWithdrawalStatus(withdrawalStatus);
        this.setOccurredOn(Instant.now());
    }

    protected void setWithdrawalId(long withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public long getWithdrawalId() {
        return this.withdrawalId;
    }

    protected void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getTransactionId() {
        return this.transactionId;
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
        return "WithdrawalProcessingTriggered{"
                + "withdrawalId=" + this.getWithdrawalId()
                + ", transactionId=" + this.getTransactionId()
                + ", withdrawalStatus=" + this.getWithdrawalStatus()
                + ", occurredOn=" + this.getOccurredOn()
                + '}';
    }
}
