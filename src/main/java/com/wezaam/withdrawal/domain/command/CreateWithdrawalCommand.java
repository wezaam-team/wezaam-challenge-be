package com.wezaam.withdrawal.domain.command;

import java.time.Instant;

import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateWithdrawalCommand {
    private Long userId;
    private Long paymentMethodId;
    private Double amount;
    private Instant createAt;
    private Instant executeAt;
    private WithdrawalStatus status;
    private WithdrawalType type;
}
