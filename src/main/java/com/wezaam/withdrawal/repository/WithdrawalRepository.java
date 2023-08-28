package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.WithdrawalInstant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<WithdrawalInstant, Long> {
}
