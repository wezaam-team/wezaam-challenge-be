package com.weezam.challenge.user.domain.repository;

import java.util.List;
import java.util.Optional;

import com.weezam.challenge.user.domain.model.User;

public interface UserRepository {

    List<User> findAll();

    Optional<User> findOne(final Long id);
}
