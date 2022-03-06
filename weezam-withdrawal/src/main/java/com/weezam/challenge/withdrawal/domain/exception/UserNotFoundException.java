package com.weezam.challenge.withdrawal.domain.exception;

public class UserNotFoundException extends WithdrawalApplicationException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
