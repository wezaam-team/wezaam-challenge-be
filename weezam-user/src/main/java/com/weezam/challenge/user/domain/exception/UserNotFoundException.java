package com.weezam.challenge.user.domain.exception;

public class UserNotFoundException extends Exception {

    /**
     * Constructor
     * @param message
     */
    public UserNotFoundException(final String message) {
        super(message);
    }

}
