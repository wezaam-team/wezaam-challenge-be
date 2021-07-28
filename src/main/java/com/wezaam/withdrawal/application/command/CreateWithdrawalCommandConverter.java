package com.wezaam.withdrawal.application.command;

import com.wezaam.withdrawal.application.rest.dto.CreateWithdrawalRequest;

import java.util.function.Function;

public class CreateWithdrawalCommandConverter implements Function<CreateWithdrawalRequest, CreateWithdrawalCommand> {

    private CreateWithdrawalCommandConverter() {
        super();
    }

    public static CreateWithdrawalCommandConverter aCreateWithdrawalCommandConverter() {
        return new CreateWithdrawalCommandConverter();
    }

    @Override
    public CreateWithdrawalCommand apply(CreateWithdrawalRequest createWithdrawalRequest) {
        return CreateWithdrawalCommandBuilder.aCreateWithdrawalCommandBuilder()
                .withPaymentMethodId(createWithdrawalRequest.getPaymentMethodId())
                .withUserId(createWithdrawalRequest.getUserId())
                .withAmount(createWithdrawalRequest.getAmount())
                .withImmediate(createWithdrawalRequest.getImmediate())
                .withScheduledFor(createWithdrawalRequest.getScheduledFor())
                .build();
    }
}
