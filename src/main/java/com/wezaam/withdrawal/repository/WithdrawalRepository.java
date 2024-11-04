package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.enums.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
    List<Withdrawal> findAllByExecuteAtBeforeAndStatus(Instant date, WithdrawalStatus status);
}
