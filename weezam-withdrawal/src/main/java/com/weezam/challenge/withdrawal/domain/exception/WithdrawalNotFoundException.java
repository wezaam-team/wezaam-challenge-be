package com.weezam.challenge.withdrawal.domain.exception;

public class WithdrawalNotFoundException extends WithdrawalApplicationException {

    /**
     * Constructor
     * @param message
     */
    public WithdrawalNotFoundException(final String message) {
        super(message);
    }

}
