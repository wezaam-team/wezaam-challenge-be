package com.weezam.challenge.user.adapter.out.persistence;

import com.weezam.challenge.user.adapter.out.persistence.entity.PaymentMethodEntity;
import com.weezam.challenge.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepositoryJpa extends JpaRepository<PaymentMethodEntity, Long> {
}
