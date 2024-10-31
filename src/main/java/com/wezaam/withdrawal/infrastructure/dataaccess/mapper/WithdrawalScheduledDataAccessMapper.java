package com.wezaam.withdrawal.infrastructure.dataaccess.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;

@Component
public class WithdrawalScheduledDataAccessMapper {

    public List<WithdrawalScheduled> mapAsWithdrawalScheduledDomainList(
            List<com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled>
                    withdrawalScheduledList) {
        return withdrawalScheduledList.stream().map(this::mapAsWithdrawalDomain).toList();
    }

    public WithdrawalScheduled mapAsWithdrawalDomain(
            com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled
                    withdrawalScheduled) {
        return WithdrawalScheduled.builder()
                .id(withdrawalScheduled.getId())
                .transactionId(withdrawalScheduled.getTransactionId())
                .userId(withdrawalScheduled.getUserId())
                .paymentMethodId(withdrawalScheduled.getPaymentMethodId())
                .status(withdrawalScheduled.getStatus())
                .createdAt(withdrawalScheduled.getCreatedAt())
                .amount(withdrawalScheduled.getAmount())
                .executeAt(withdrawalScheduled.getExecuteAt())
                .build();
    }

    public com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled
            mapWithdrawalEntity(WithdrawalScheduled withdrawalScheduled) {
        com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled
                withdrawalToSave =
                        new com.wezaam.withdrawal.infrastructure.dataaccess.entity
                                .WithdrawalScheduled();

        withdrawalToSave.setId(withdrawalScheduled.getId());
        withdrawalToSave.setTransactionId(withdrawalScheduled.getTransactionId());
        withdrawalToSave.setAmount(withdrawalScheduled.getAmount());
        withdrawalToSave.setCreatedAt(withdrawalScheduled.getCreatedAt());
        withdrawalToSave.setUserId(withdrawalScheduled.getUserId());
        withdrawalToSave.setPaymentMethodId(withdrawalScheduled.getPaymentMethodId());
        withdrawalToSave.setStatus(withdrawalScheduled.getStatus());
        withdrawalToSave.setExecuteAt(withdrawalScheduled.getExecuteAt());

        return withdrawalToSave;
    }
}
