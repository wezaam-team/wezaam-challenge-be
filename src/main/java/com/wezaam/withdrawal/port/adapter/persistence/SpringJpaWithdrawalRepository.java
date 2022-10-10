package com.wezaam.withdrawal.port.adapter.persistence;

import com.wezaam.withdrawal.domain.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaWithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
