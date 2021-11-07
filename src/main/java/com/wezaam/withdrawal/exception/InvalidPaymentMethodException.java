package com.wezaam.withdrawal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid payment method")
public class InvalidPaymentMethodException extends RuntimeException {

    private final Long paymentMethodId;

    public InvalidPaymentMethodException(Long paymentMethodId) {
        super(String.format("Payment method is not valid: %d", paymentMethodId));
        this.paymentMethodId = paymentMethodId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }
}
