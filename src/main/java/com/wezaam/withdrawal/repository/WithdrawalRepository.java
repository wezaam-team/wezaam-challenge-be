package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {

    @Query("From withdrawals w where w.sentAt is null and w.status <> 'PENDING'")
    List<Withdrawal> findAllToBeSent();
}
