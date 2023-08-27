package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.InstantWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<InstantWithdrawal, Long> {
}
