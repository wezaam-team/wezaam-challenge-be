package com.weezam.challenge.user.domain;

import java.util.List;

import com.weezam.challenge.user.domain.model.User;
import com.weezam.challenge.user.domain.exception.InvalidCriteriaException;
import com.weezam.challenge.user.domain.exception.UserNotFoundException;

public interface UserAggregate {

    List<User> findAll();

    User findOne(final Long id) throws UserNotFoundException, InvalidCriteriaException;
}
