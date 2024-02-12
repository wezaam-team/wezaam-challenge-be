package com.wezaam.withdrawal.application.command;

import com.wezaam.withdrawal.domain.Withdrawal;

import java.util.function.Function;

public class FinishWithdrawalProcessingCommandFromDomainConverter implements Function<Withdrawal, FinishWithdrawalProcessingCommand> {

    private FinishWithdrawalProcessingCommandFromDomainConverter() {
        super();
    }

    public static FinishWithdrawalProcessingCommandFromDomainConverter aFinishWithdrawalProcessingCommandFromDomainConverter() {
        return new FinishWithdrawalProcessingCommandFromDomainConverter();
    }

    @Override
    public FinishWithdrawalProcessingCommand apply(Withdrawal withdrawal) {
        return FinishWithdrawalProcessingCommandBuilder
                .aFinishWithdrawalProcessingCommandBuilder()
                .withId(withdrawal.getId())
                .withScheduledFor(withdrawal.getScheduledFor())
                .withAmount(withdrawal.getAmount())
                .withImmediate(withdrawal.getImmediate())
                .withWithdrawalStatus(withdrawal.getWithdrawalStatus())
                .withUserId(withdrawal.getUser().getId())
                .withPaymentMethodId(withdrawal.getPaymentMethod().getId())
                .build();
    }
}
