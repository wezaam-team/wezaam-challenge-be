package com.wezaam.withdrawal.infrastructure.dataaccess.mapper;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.entity.PaymentMethod;

@Component
public class PaymentMethodDataAccessMapper {

    public PaymentMethod mapAsPaymentMethod(
            com.wezaam.withdrawal.infrastructure.dataaccess.entity.PaymentMethod paymentMethod) {
        return PaymentMethod.builder()
                .id(paymentMethod.getId())
                .name(paymentMethod.getName())
                .build();
    }
}
