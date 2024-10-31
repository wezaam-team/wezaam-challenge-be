package com.wezaam.withdrawal.infrastructure.dataaccess.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.time.Instant;

import jakarta.persistence.*;

import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;

@Entity(name = "withdrawals")
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long transactionId;
    private Double amount;
    private Instant createdAt;
    private Long userId;
    private Long paymentMethodId;

    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawalStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTransactionId() {
        return transactionId;
    }
}
