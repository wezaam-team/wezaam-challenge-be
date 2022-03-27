package com.wezaam.withdrawal.exception;

public class PaymentMethodNotFoundException extends Exception{

    public PaymentMethodNotFoundException(String message) {
        super(message);
    }
}
