package com.wezaam.withdrawal.domain.repositories;

import com.wezaam.withdrawal.domain.entities.Employee;
import com.wezaam.withdrawal.domain.entities.Withdraw;
import com.wezaam.withdrawal.domain.entities.WithdrawStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {


    Page<Withdraw> findByStatus(WithdrawStatusEnum status, Pageable pageable);

    Page<Withdraw> findByStatusAndExecutionDate(WithdrawStatusEnum created, LocalDate now, Pageable pageable);

    boolean existsByEmployeeAndStatus(Employee employee, WithdrawStatusEnum created);
}
