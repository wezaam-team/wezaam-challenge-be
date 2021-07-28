package com.wezaam.withdrawal.domain.converter;

import com.wezaam.withdrawal.application.command.WithdrawalCommand;
import com.wezaam.withdrawal.domain.Withdrawal;
import com.wezaam.withdrawal.domain.WithdrawalBuilder;

import java.util.function.Function;

public class WithdrawalFromCommandConverter implements Function<WithdrawalCommand, Withdrawal> {

    private WithdrawalFromCommandConverter() {
        super();
    }

    public static WithdrawalFromCommandConverter aWithdrawalFromCommandConverter() {
        return new WithdrawalFromCommandConverter();
    }

    @Override
    public Withdrawal apply(WithdrawalCommand withdrawalCommand) {
        return WithdrawalBuilder.aWithdrawalBuilder()
                .withId(withdrawalCommand.getId())
                .withAmount(withdrawalCommand.getAmount())
                .withImmediate(withdrawalCommand.getImmediate())
                .withScheduledFor(withdrawalCommand.getScheduledFor())
                .withWithdrawalStatus(withdrawalCommand.getWithdrawalStatus())
                .build();
    }
}
