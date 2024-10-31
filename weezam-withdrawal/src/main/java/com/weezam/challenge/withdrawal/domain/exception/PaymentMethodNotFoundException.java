package com.weezam.challenge.withdrawal.domain.exception;

public class PaymentMethodNotFoundException extends WithdrawalApplicationException {

    public PaymentMethodNotFoundException(String message) {
        super(message);
    }
}
