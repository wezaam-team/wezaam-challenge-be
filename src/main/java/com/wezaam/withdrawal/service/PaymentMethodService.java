package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.NotFoundException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethod findById(Long paymentMethodId) {
        return paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new NotFoundException("Payment method not found for the id:" + paymentMethodId));
    }
}
