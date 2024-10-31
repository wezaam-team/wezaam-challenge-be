package com.wezaam.withdrawal.infrastructure.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wezaam.withdrawal.infrastructure.dataaccess.entity.PaymentMethod;

@Repository
public interface PaymentMethodJpaRepository extends JpaRepository<PaymentMethod, Long> {}
