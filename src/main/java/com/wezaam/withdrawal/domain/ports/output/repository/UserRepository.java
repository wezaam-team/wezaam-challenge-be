package com.wezaam.withdrawal.domain.ports.output.repository;

import java.util.List;
import java.util.Optional;

import com.wezaam.withdrawal.domain.entity.User;

public interface UserRepository {

    Optional<User> findById(Long userId);

    List<User> findAll();
}
