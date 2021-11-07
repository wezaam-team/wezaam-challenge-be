package com.wezaam.withdrawal.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

import static javax.persistence.GenerationType.TABLE;

@Entity(name = "withdrawals")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = TABLE)
    private Long id;
    private Long transactionId;
    private BigDecimal amount;
    private Instant createdAt;
    private Instant sentAt;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
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

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTransactionId() {
        return transactionId;
    }
}
