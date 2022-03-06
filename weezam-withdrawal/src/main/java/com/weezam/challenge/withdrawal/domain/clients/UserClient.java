package com.weezam.challenge.withdrawal.domain.clients;

import com.weezam.challenge.withdrawal.domain.exception.UserNotFoundException;
import com.weezam.challenge.withdrawal.domain.model.User;

import java.util.Optional;

public interface UserClient {

    Optional<User> findOne(final Long id) throws UserNotFoundException;
}
