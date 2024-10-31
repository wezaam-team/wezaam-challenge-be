package com.wezaam.withdrawal.domain.event;

import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalEvent {
    private Long id;
    private Long paymentMethodId;
    private WithdrawalType withdrawalType;
}
