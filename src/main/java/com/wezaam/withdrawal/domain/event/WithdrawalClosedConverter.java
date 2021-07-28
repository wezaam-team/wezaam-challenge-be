package com.wezaam.withdrawal.domain.event;

import com.wezaam.withdrawal.domain.Withdrawal;

import java.util.function.Function;

public class WithdrawalClosedConverter implements Function<Withdrawal, WithdrawalClosed> {

    private WithdrawalClosedConverter() {
        super();
    }

    public static WithdrawalClosedConverter aWithdrawalClosedConverter() {
        return new WithdrawalClosedConverter();
    }

    @Override
    public WithdrawalClosed apply(Withdrawal withdrawal) {
        return WithdrawalClosedBuilder
                .aWithdrawalClosedBuilder()
                .withAmount(withdrawal.getAmount())
                .withUserId(withdrawal.getUser().getId())
                .withId(withdrawal.getId())
                .withPaymentMethodId(withdrawal.getPaymentMethod().getId())
                .withWithdrawalStatus(withdrawal.getWithdrawalStatus())
                .withImmediate(withdrawal.getImmediate())
                .withScheduledFor(withdrawal.getScheduledFor())
                .build();
    }
}

