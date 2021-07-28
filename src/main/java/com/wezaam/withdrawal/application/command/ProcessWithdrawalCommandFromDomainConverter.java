package com.wezaam.withdrawal.application.command;

import com.wezaam.withdrawal.domain.Withdrawal;

import java.util.function.Function;

public class ProcessWithdrawalCommandFromDomainConverter implements Function<Withdrawal, ProcessWithdrawalCommand> {

    private ProcessWithdrawalCommandFromDomainConverter() {
        super();
    }

    public static ProcessWithdrawalCommandFromDomainConverter aProcessWithdrawalCommandConverter() {
        return new ProcessWithdrawalCommandFromDomainConverter();
    }

    @Override
    public ProcessWithdrawalCommand apply(Withdrawal withdrawal) {
        return ProcessWithdrawalCommandBuilder
                .aProcessWithdrawalCommandBuilder()
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
