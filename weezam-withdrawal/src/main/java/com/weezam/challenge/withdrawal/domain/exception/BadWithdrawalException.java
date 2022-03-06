package com.weezam.challenge.withdrawal.domain.exception;

public class BadWithdrawalException extends WithdrawalApplicationException {

    /**
     * Constructor
     * @param message
     */
    public BadWithdrawalException(final String message) {
        super(message);
    }

}
