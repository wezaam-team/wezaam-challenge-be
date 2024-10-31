package com.weezam.challenge.withdrawal.domain.exception;

public class WithdrawalApplicationException extends Exception {

    public WithdrawalApplicationException(String message) {
        super(message);
    }

    public WithdrawalApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
