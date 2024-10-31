package com.weezam.challenge.user.domain.exception;

public class PaymentMethodNotFoundException extends Exception {

    /**
     * Constructor
     * @param message
     */
    public PaymentMethodNotFoundException(final String message) {
        super(message);
    }

}
