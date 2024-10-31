package com.wezaam.withdrawal.infrastructure.rest.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WithdrawalResponse {
    private Long id;
    private Long transactionId;
    private Double amount;
    private Instant createdAt;
    private Instant executeAt;
    private Long userId;
    private Long paymentMethodId;
    private WithdrawalStatus status;
}
