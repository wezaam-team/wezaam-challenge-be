package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
