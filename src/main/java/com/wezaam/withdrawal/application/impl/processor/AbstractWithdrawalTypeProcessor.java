package com.wezaam.withdrawal.application.impl.processor;

import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractWithdrawalTypeProcessor {

    public abstract WithdrawalType getType();

    protected void logError(String message, Exception e, Object... params) {
        log.error(message, params);
        log.error(e.getMessage(), e);
    }
}
