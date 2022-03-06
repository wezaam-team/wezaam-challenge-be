package com.weezam.challenge.user.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    private Long id;
    private String firstName;
    private List<PaymentMethod> paymentMethods;
    private Double maxWithdrawalAmount;
    
}
