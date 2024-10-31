package com.wezaam.withdrawal.infrastructure.dataaccess.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.infrastructure.dataaccess.entity.WithdrawalScheduled;

@Repository
public interface WithdrawalScheduledJpaRepository extends JpaRepository<WithdrawalScheduled, Long> {

    List<WithdrawalScheduled> findAllByExecuteAtBefore(Instant date);

    List<WithdrawalScheduled> findAllByExecuteAtBeforeAndStatus(
            Instant date, WithdrawalStatus status);
}
