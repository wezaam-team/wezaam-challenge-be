package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.WithdrawalAsap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<WithdrawalAsap, Long> {
}
