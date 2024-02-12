package com.weezam.challenge.withdrawal.adapter.in.rest.dto;

import com.weezam.challenge.withdrawal.domain.model.WithdrawalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewWithdrawalCommandDto {
    private Double amount;
    private Instant executeAt;
    private Long userId;
    private Long paymentMethodId;

}
