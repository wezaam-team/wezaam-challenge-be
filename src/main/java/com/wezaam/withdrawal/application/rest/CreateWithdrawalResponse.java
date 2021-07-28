package com.wezaam.withdrawal.application.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class CreateWithdrawalResponse {

    Long id;

    Long userId;

    Long paymentMethodId;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "#,###.00")
    BigDecimal amount;

    private Boolean immediate;

    private Instant scheduledFor;

    private WithdrawalStatus withdrawalStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getImmediate() {
        return immediate;
    }

    public void setImmediate(Boolean immediate) {
        this.immediate = immediate;
    }

    public Instant getScheduledFor() {
        return scheduledFor;
    }

    public void setScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
    }

    public WithdrawalStatus getWithdrawalStatus() {
        return withdrawalStatus;
    }

    public void setWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }
}
