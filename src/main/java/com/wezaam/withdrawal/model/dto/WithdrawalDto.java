package com.wezaam.withdrawal.model.dto;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.enums.WithdrawalStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class WithdrawalDto {

    private Long id;
    private Long transactionId;
    private Double amount;
    private Instant createdAt;
    private Instant executeAt;
    private Long userId;
    private Long paymentMethodId;
    private WithdrawalStatus status;

    public static WithdrawalDto build(Withdrawal withdrawal) {
        return WithdrawalDto.builder()
                .id(withdrawal.getId())
                .transactionId(withdrawal.getTransactionId())
                .amount(withdrawal.getAmount())
                .createdAt(withdrawal.getCreatedAt())
                .executeAt(withdrawal.getCreatedAt())
                .userId(withdrawal.getUserId())
                .paymentMethodId(withdrawal.getPaymentMethodId())
                .status(withdrawal.getStatus())
                .build();
    }
}
