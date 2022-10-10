package com.wezaam.withdrawal.port.adapter.persistence;

import com.wezaam.withdrawal.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaUserRepository extends JpaRepository<User, Long> {
}
