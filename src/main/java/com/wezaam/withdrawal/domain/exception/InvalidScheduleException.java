package com.wezaam.withdrawal.domain.exception;

public class InvalidScheduleException extends Exception {
    public InvalidScheduleException() {
    }

    public InvalidScheduleException(String message) {
        super(message);
    }

    public InvalidScheduleException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidScheduleException(Throwable cause) {
        super(cause);
    }

    public InvalidScheduleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
