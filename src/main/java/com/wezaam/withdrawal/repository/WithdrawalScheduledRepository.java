package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.ScheduleWithdrawal;
import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalEntity;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface WithdrawalScheduledRepository extends JpaRepository<WithdrawalScheduled, Long> {
    List<WithdrawalScheduled> findAllByExecuteAtBefore(Instant date);
    @Query( value = "SELECT * FROM scheduled_withdrawals w WHERE w.status = 'FAILED'",
            nativeQuery = true)
    List<WithdrawalScheduled> findAllByFailedStatus();
}
