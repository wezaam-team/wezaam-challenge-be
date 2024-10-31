package com.wezaam.withdrawal.infrastructure.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wezaam.withdrawal.infrastructure.dataaccess.entity.Withdrawal;

@Repository
public interface WithdrawalJpaRepository extends JpaRepository<Withdrawal, Long> {}
