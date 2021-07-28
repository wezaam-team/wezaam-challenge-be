package com.wezaam.withdrawal.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {

    List<Withdrawal> findAllByScheduledForBefore(Instant date);
}
