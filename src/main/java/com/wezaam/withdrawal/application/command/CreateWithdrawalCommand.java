package com.wezaam.withdrawal.application.command;

import com.wezaam.withdrawal.domain.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class CreateWithdrawalCommand extends WithdrawalCommand {

    protected CreateWithdrawalCommand(Long id, Long userId, Long paymentMethodId, BigDecimal amount, Boolean immediate, Instant scheduledFor, WithdrawalStatus withdrawalStatus) {
        super(id, userId, paymentMethodId, amount, immediate, scheduledFor, withdrawalStatus);
    }
}
