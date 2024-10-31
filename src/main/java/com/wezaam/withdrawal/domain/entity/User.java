package com.wezaam.withdrawal.domain.entity;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private Double maxWithdrawalAmount;
    private List<PaymentMethod> paymentMethods;
}
