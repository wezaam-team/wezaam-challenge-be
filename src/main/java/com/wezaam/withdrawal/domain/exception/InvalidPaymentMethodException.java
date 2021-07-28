package com.wezaam.withdrawal.domain.exception;

public class InvalidPaymentMethodException extends Exception {
    public InvalidPaymentMethodException() {
    }

    public InvalidPaymentMethodException(String message) {
        super(message);
    }

    public InvalidPaymentMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPaymentMethodException(Throwable cause) {
        super(cause);
    }

    public InvalidPaymentMethodException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
