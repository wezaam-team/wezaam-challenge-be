package com.wezaam.withdrawal.domain.event;

import com.wezaam.withdrawal.domain.Withdrawal;

import java.util.function.Function;

public class WithdrawalInvalidatedConverter implements Function<Withdrawal, WithdrawalInvalidated> {
    private WithdrawalInvalidatedConverter() {
        super();
    }

    public static WithdrawalInvalidatedConverter aWithdrawalInvalidatedConverter() {
        return new WithdrawalInvalidatedConverter();
    }

    @Override
    public WithdrawalInvalidated apply(Withdrawal withdrawal) {
        return WithdrawalInvalidatedBuilder
                .aWithdrawalInvalidatedBuilder()
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
