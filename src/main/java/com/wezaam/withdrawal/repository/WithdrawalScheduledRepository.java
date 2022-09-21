package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.WithdrawalScheduled;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalScheduledRepository extends JpaRepository<WithdrawalScheduled, Long> {

    List<WithdrawalScheduled> findAllByExecuteAtBefore(Instant date);
}
