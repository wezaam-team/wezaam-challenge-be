package com.wezaam.withdrawal.infrastructure.rest.dto.create;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateWithdrawalRequest {

    @NotNull(message = "UserId must be required")
    private Long userId;

    @NotNull(message = "paymentMethodId must be required")
    private Long paymentMethodId;

    @NotNull(message = "Amount must be required")
    private BigDecimal amount;

    @NotNull(message = "executeAt must be required")
    private String executeAt;
}
