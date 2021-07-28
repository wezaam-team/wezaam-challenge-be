package com.wezaam.withdrawal.domain.event;

import com.wezaam.withdrawal.domain.Withdrawal;

import java.util.function.Function;

public class WithdrawalCreatedConverter implements Function<Withdrawal, WithdrawalCreated> {

    private WithdrawalCreatedConverter() {
        super();
    }

    public static WithdrawalCreatedConverter aWithdrawalCreatedConverter() {
        return new WithdrawalCreatedConverter();
    }

    @Override
    public WithdrawalCreated apply(Withdrawal withdrawal) {
        return WithdrawalCreatedBuilder
                .aWithdrawalCreatedBuilder()
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
