package com.wezaam.withdrawal.infrastructure.dataaccess.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.entity.Withdrawal;

@Component
public class WithdrawalDataAccessMapper {

    public List<Withdrawal> mapWithdrawalListAsWithdrawalResponseList(
            List<com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal>
                    withdrawalList) {
        return withdrawalList.stream().map(this::mapWithdrawalAsWithdrawalResponse).toList();
    }

    public Withdrawal mapWithdrawalAsWithdrawalResponse(
            com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal withdrawal) {
        return Withdrawal.builder()
                .id(withdrawal.getId())
                .transactionId(withdrawal.getTransactionId())
                .userId(withdrawal.getUserId())
                .paymentMethodId(withdrawal.getPaymentMethodId())
                .status(withdrawal.getStatus())
                .createdAt(withdrawal.getCreatedAt())
                .amount(withdrawal.getAmount())
                .build();
    }

    public com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal mapWithdrawalEntity(
            Withdrawal withdrawal) {
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal withdrawalToSave =
                new com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal();

        withdrawalToSave.setId(withdrawal.getId());
        withdrawalToSave.setTransactionId(withdrawal.getTransactionId());
        withdrawalToSave.setAmount(withdrawal.getAmount());
        withdrawalToSave.setCreatedAt(withdrawal.getCreatedAt());
        withdrawalToSave.setUserId(withdrawal.getUserId());
        withdrawalToSave.setPaymentMethodId(withdrawal.getPaymentMethodId());
        withdrawalToSave.setStatus(withdrawal.getStatus());

        return withdrawalToSave;
    }
}
