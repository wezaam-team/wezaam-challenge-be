package com.weezam.challenge.notifications.domain.exception;

public class SendNotificationException extends Exception {

    /**
     * Constructor
     * @param message
     */
    public SendNotificationException(final String message) {
        super(message);
    }

}
