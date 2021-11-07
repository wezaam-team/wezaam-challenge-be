package com.wezaam.withdrawal.rest;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class WithdrawalRequest {

    @NotNull
    private Long userId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String executeAt;
    @NotNull
    private Long paymentMethodId;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getExecuteAt() {
        return executeAt;
    }

    public void setExecuteAt(String executeAt) {
        this.executeAt = executeAt;
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
}
