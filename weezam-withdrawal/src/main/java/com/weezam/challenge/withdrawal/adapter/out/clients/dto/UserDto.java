package com.weezam.challenge.withdrawal.adapter.out.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String firstName;
    private List<PaymentMethodDto> paymentMethods;
    private Double maxWithdrawalAmount;
    
}
