package com.wezaam.withdrawal.domain;

import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.model.WithdrawalExecution;
import com.wezaam.withdrawal.model.WithdrawalStatus;

import java.io.Serializable;
import java.time.Instant;

public class WithdrawalProcessingResponse implements Serializable {

    private static final long serialVersionUID = 7715251246335544990L;

    private WithdrawalStatus status;
    private WithdrawalExecution execution;
    private Long transactionId;
    private PaymentMethod paymentMethod;
    private User user;
    private Instant executedAt;

    public WithdrawalStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawalStatus status) {
        this.status = status;
    }

    public WithdrawalExecution getExecution() {
        return execution;
    }

    public void setExecution(WithdrawalExecution execution) {
        this.execution = execution;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Instant executedAt) {
        this.executedAt = executedAt;
    }
}
