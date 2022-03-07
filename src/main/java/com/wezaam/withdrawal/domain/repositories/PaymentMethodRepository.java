package com.wezaam.withdrawal.domain.repositories;

import com.wezaam.withdrawal.domain.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
