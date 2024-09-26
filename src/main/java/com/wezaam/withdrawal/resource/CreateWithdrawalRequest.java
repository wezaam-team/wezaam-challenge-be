package com.wezaam.withdrawal.resource;

public class CreateWithdrawalRequest {

    private Long userId;
    private Long paymentMethodId;
    private Double amount;
    private String executeAt;

    public CreateWithdrawalRequest() {}

    public CreateWithdrawalRequest(Long userId, Long paymentMethodId, Double amount, String executeAt) {
        this.setUserId(userId);
        this.setPaymentMethodId(paymentMethodId);
        this.setAmount(amount);
        this.setExecuteAt(executeAt);
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPaymentMethodId() {
        return this.paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getExecuteAt() {
        return this.executeAt;
    }

    public void setExecuteAt(String executeAt) {
        this.executeAt = executeAt;
    }
}
