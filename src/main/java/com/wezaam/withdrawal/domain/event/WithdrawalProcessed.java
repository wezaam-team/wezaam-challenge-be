package com.wezaam.withdrawal.domain.event;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class WithdrawalProcessed extends WithdrawalEvent {
    public WithdrawalProcessed() {
        this(null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    protected WithdrawalProcessed(Long id, Long userId, Long paymentMethodId, BigDecimal amount, Boolean immediate, Instant scheduledFor, WithdrawalStatus withdrawalStatus) {
        super(id, userId, paymentMethodId, amount, immediate, scheduledFor, withdrawalStatus);
    }
}
