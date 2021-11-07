package com.wezaam.withdrawal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Payment method not found")
public class NoSuchPaymentMethodException extends RuntimeException {

    private final Long paymentMethodId;

    public NoSuchPaymentMethodException(Long paymentMethodId) {
        super(String.format("Payment method does not exist: %d", paymentMethodId));
        this.paymentMethodId = paymentMethodId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }
}
