package com.weezam.challenge.withdrawal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Withdrawal {

    private Long id;
    private Long transactionId;
    private Double amount;
    private Instant createdAt;
    private Instant executeAt;
    private Long userId;
    private Long paymentMethodId;
    private WithdrawalStatus status;
}
