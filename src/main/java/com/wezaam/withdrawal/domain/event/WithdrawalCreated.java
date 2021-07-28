package com.wezaam.withdrawal.domain.event;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class WithdrawalCreated extends WithdrawalEvent {
    public WithdrawalCreated() {
        super();
    }

    public WithdrawalCreated(Long id, Long userId, Long paymentMethodId, BigDecimal amount, Boolean immediate, Instant scheduledFor, WithdrawalStatus withdrawalStatus) {
        super(id, userId, paymentMethodId, amount, immediate, scheduledFor, withdrawalStatus);
    }
}
