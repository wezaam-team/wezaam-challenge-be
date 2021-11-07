package com.wezaam.withdrawal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found")
public class NoSuchUserException extends RuntimeException {

    private final Long userId;

    public NoSuchUserException(Long userId) {
        super(String.format("User does not exist: %d", userId));
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
