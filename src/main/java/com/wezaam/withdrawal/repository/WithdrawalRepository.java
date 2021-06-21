package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.WithdrawalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WithdrawalRepository extends JpaRepository<WithdrawalEntity, Long> {
    @Query( value = "SELECT * FROM withdrawals w WHERE w.status = 'FAILED'",
            nativeQuery = true)
    List<WithdrawalEntity> findAllByFailedStatus();
}
