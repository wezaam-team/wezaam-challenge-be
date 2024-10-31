package com.weezam.challenge.withdrawal.adapter.out.persistence;

import com.weezam.challenge.withdrawal.adapter.out.persistence.entity.WithdrawalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalRepositoryJpa extends JpaRepository<WithdrawalEntity, Long> {
}
