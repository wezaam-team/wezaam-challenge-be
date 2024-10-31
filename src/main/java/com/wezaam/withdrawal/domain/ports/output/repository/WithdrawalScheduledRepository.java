package com.wezaam.withdrawal.domain.ports.output.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;

public interface WithdrawalScheduledRepository {

    Optional<WithdrawalScheduled> findById(Long withdrawalScheduledId);

    WithdrawalScheduled save(WithdrawalScheduled withdrawalScheduled);

    List<WithdrawalScheduled> findAll();

    List<WithdrawalScheduled> findAllByExecuteAtBefore(Instant instant);

    List<WithdrawalScheduled> findAllByExecuteAtBeforeWithStatus(
            Instant instant, WithdrawalStatus status);
}
