package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository repository;


    public boolean existsById(Long paymentMethodId) {
        return repository.existsById(paymentMethodId);
    }
}
