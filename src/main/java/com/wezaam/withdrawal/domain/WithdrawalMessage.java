package com.wezaam.withdrawal.domain;

import java.io.Serializable;

public class WithdrawalMessage implements Serializable {

    private static final long serialVersionUID = 7308455153886233016L;

    private Long transactionId;
    private Long paymentMethodId;
    private Long userId;
    private String message;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
