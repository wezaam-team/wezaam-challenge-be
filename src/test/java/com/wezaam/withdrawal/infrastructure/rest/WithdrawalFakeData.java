package com.wezaam.withdrawal.infrastructure.rest;

import java.math.BigDecimal;

import com.wezaam.withdrawal.infrastructure.rest.dto.create.CreateWithdrawalRequest;

public class WithdrawalFakeData extends ExtendedControllerTest {

    protected static CreateWithdrawalRequest getWithdrawalRequestAsap() {
        return CreateWithdrawalRequest.builder()
                .userId(100L)
                .paymentMethodId(1L)
                .amount(BigDecimal.valueOf(400, 0))
                .executeAt("ASAP")
                .build();
    }

    protected static CreateWithdrawalRequest getWithdrawalRequestScheduled() {
        return CreateWithdrawalRequest.builder()
                .userId(100L)
                .paymentMethodId(1L)
                .amount(BigDecimal.valueOf(400, 0))
                .executeAt("2024-11-30T18:35:24.00Z")
                .build();
    }
}
