package com.wezaam.withdrawal.domain.model;

import com.wezaam.common.domain.model.DomainEvent;
import java.time.Instant;

public class WithdrawalProcessingTriggeringFailed implements DomainEvent {
    private long withdrawalId;
    private String failureMessage;
    private WithdrawalStatus withdrawalStatus;
    private Instant occurredOn;

    protected WithdrawalProcessingTriggeringFailed() {}

    public WithdrawalProcessingTriggeringFailed(
            long withdrawalId, String failureMessage, WithdrawalStatus withdrawalStatus) {
        this.setWithdrawalId(withdrawalId);
        this.setFailureMessage(failureMessage);
        this.setWithdrawalStatus(withdrawalStatus);
        this.setOccurredOn(Instant.now());
    }

    protected void setWithdrawalId(long withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public long getWithdrawalId() {
        return this.withdrawalId;
    }

    protected void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public String getFailureMessage() {
        return this.failureMessage;
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
        return "WithdrawalProcessingTriggeringFailed{"
                + "withdrawalId=" + this.getWithdrawalId()
                + ", failureMessage=" + this.getFailureMessage()
                + ", withdrawalStatus=" + this.getWithdrawalStatus()
                + ", occurredOn=" + this.getOccurredOn()
                + '}';
    }
}
