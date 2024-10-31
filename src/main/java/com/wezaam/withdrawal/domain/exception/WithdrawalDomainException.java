package com.wezaam.withdrawal.domain.exception;

public class WithdrawalDomainException extends RuntimeException {

    public WithdrawalDomainException(String message) {
        super(message);
    }

    public WithdrawalDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
