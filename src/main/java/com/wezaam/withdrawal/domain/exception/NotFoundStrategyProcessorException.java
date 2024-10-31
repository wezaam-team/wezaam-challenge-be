package com.wezaam.withdrawal.domain.exception;

public class NotFoundStrategyProcessorException extends RuntimeException {

    public NotFoundStrategyProcessorException(String message) {
        super(message);
    }

    public NotFoundStrategyProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
