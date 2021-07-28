package com.wezaam.withdrawal.application.rest;

import com.wezaam.withdrawal.domain.Withdrawal;

import java.util.function.Function;

public class CreateWithdrawalResponseConverter implements Function<Withdrawal, CreateWithdrawalResponse> {

    private CreateWithdrawalResponseConverter() {
        super();
    }

    public static CreateWithdrawalResponseConverter aCreateWithdrawalResponseConverter() {
        return new CreateWithdrawalResponseConverter();
    }

    @Override
    public CreateWithdrawalResponse apply(Withdrawal withdrawal) {
        return CreateWithdrawalResponseBuilder
                .aCreateWithdrawalResponseBuilder()
                .withId(withdrawal.getId())
                .withPaymentMethodId(withdrawal.getUser().getId())
                .withUserId(withdrawal.getPaymentMethod().getId())
                .withAmount(withdrawal.getAmount())
                .withImmediate(withdrawal.getImmediate())
                .withScheduledFor(withdrawal.getScheduledFor())
                .withWithdrawalStatus(withdrawal.getWithdrawalStatus())
                .build();
    }
}
