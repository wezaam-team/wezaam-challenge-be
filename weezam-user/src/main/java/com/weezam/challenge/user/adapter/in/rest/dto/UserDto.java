package com.weezam.challenge.user.adapter.in.rest.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String firstName;
    private List<PaymentMethodDto> paymentMethods;
    private Double maxWithdrawalAmount;
    
}
