package com.weezam.challenge.withdrawal.domain.exception;

public class InvalidCriteriaException extends WithdrawalApplicationException {

    /**
     * Constrtuctor.
     * @param message
     */
    public InvalidCriteriaException(final String message) {
        super(message);
    }
    
}
