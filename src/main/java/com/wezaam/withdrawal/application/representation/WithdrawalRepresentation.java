package com.wezaam.withdrawal.application.representation;

import com.wezaam.withdrawal.domain.model.Withdrawal;
import java.time.Instant;

public class WithdrawalRepresentation {

    private long id;
    private long userId;
    private long paymentMethodId;
    private double amount;
    private Instant createdAt;
    private Instant executeAt;
    private Long transactionId;
    private String status;

    public WithdrawalRepresentation(Withdrawal withdrawal) {
        this.setId(withdrawal.getId());
        this.setUserId(withdrawal.getUserId());
        this.setPaymentMethodId(withdrawal.getPaymentMethodId());
        this.setAmount(withdrawal.getAmount());
        this.setCreatedAt(withdrawal.getCreatedAt());
        this.setExecuteAt(withdrawal.getExecuteAt());
        this.setTransactionId(withdrawal.getTransactionId());
        this.setStatus(withdrawal.getStatus().toString());
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setPaymentMethodId(long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public long getPaymentMethodId() {
        return this.paymentMethodId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setExecuteAt(Instant executeAt) {
        this.executeAt = executeAt;
    }

    public Instant getExecuteAt() {
        return this.executeAt;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
