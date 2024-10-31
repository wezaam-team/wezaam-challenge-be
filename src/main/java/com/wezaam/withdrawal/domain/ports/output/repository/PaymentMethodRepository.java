package com.wezaam.withdrawal.domain.ports.output.repository;

import java.util.Optional;

import com.wezaam.withdrawal.domain.entity.PaymentMethod;

public interface PaymentMethodRepository {

    Optional<PaymentMethod> findById(Long paymentMethodId);
}
