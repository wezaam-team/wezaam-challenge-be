package com.wezaam.withdrawal.application.rest;

import com.wezaam.withdrawal.domain.Withdrawal;

import java.util.function.Function;

public class GetWithdrawalResponseConverter implements Function<Withdrawal, GetWithdrawalResponse> {

    private GetWithdrawalResponseConverter() {
        super();
    }

    public static GetWithdrawalResponseConverter aGetWithdrawalResponseConverter() {
        return new GetWithdrawalResponseConverter();
    }

    @Override
    public GetWithdrawalResponse apply(Withdrawal withdrawal) {
        return GetWithdrawalResponseBuilder.aGetWithdrawalResponseBuilder()
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
