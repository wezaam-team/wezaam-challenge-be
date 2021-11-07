package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.WithdrawalScheduled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface WithdrawalScheduledRepository extends JpaRepository<WithdrawalScheduled, Long> {

    @Query("From scheduled_withdrawals sw where sw.executeAt < :date and sw.status = 'PENDING'")
    List<WithdrawalScheduled> findAllToBeProcessedBefore(Instant date);
}
