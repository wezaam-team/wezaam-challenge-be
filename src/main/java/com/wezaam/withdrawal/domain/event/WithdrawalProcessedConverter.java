package com.wezaam.withdrawal.domain.event;

import com.wezaam.withdrawal.domain.Withdrawal;

import java.util.function.Function;

public class WithdrawalProcessedConverter implements Function<Withdrawal, WithdrawalProcessed> {
    private WithdrawalProcessedConverter() {
        super();
    }

    public static WithdrawalProcessedConverter aWithdrawalProcessedConverter() {
        return new WithdrawalProcessedConverter();
    }

    @Override
    public WithdrawalProcessed apply(Withdrawal withdrawal) {
        return WithdrawalProcessedBuilder
                .aWithdrawalProcessedBuilder()
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
