package com.wezaam.withdrawal.infrastructure.rest.dto.response;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String firstName;
    private Double maxWithdrawalAmount;
    private List<PaymentMethodResponse> paymentMethods;
}
