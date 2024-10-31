package com.wezaam.withdrawal.infrastructure.rest.mapper;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.command.CreateWithdrawalCommand;
import com.wezaam.withdrawal.domain.entity.Withdrawal;
import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;
import com.wezaam.withdrawal.infrastructure.rest.dto.create.CreateWithdrawalRequest;
import com.wezaam.withdrawal.infrastructure.rest.dto.response.WithdrawalResponse;

@Component
public class WithdrawalMapper {

    public CreateWithdrawalCommand mapAsCreateWithdrawalCommand(
            CreateWithdrawalRequest withdrawalRequest) {

        CreateWithdrawalCommand.CreateWithdrawalCommandBuilder command =
                CreateWithdrawalCommand.builder();
        command.userId(withdrawalRequest.getUserId())
                .createAt(Instant.now())
                .status(WithdrawalStatus.PENDING)
                .paymentMethodId(withdrawalRequest.getPaymentMethodId())
                .amount(withdrawalRequest.getAmount().doubleValue());

        if (WithdrawalType.ASAP.name().equals(withdrawalRequest.getExecuteAt())) {
            command.type(WithdrawalType.ASAP);
        } else {
            command.executeAt(Instant.parse(withdrawalRequest.getExecuteAt()))
                    .type(WithdrawalType.SCHEDULED);
        }

        return command.build();
    }

    public List<WithdrawalResponse> mapWithdrawalListAsWithdrawalResponseList(
            List<Withdrawal> withdrawalList) {
        return withdrawalList.stream().map(this::mapWithdrawalAsWithdrawalResponse).toList();
    }

    public WithdrawalResponse mapWithdrawalAsWithdrawalResponse(Withdrawal withdrawal) {
        WithdrawalResponse.WithdrawalResponseBuilder builder = WithdrawalResponse.builder();
        builder.id(withdrawal.getId())
                .transactionId(withdrawal.getTransactionId())
                .userId(withdrawal.getUserId())
                .paymentMethodId(withdrawal.getPaymentMethodId())
                .status(withdrawal.getStatus())
                .createdAt(withdrawal.getCreatedAt())
                .amount(withdrawal.getAmount());
        if (withdrawal instanceof WithdrawalScheduled ws) {
            builder.executeAt(ws.getExecuteAt());
        }

        return builder.build();
    }
}
