package com.wezaam.withdrawal.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationException extends RuntimeException {

    private String message;
}
