package com.weezam.challenge.user.domain.aggregate;

import java.util.List;
import java.util.Objects;

import com.weezam.challenge.user.domain.UserAggregate;
import com.weezam.challenge.user.domain.model.User;
import com.weezam.challenge.user.domain.exception.InvalidCriteriaException;
import com.weezam.challenge.user.domain.exception.UserNotFoundException;
import com.weezam.challenge.user.domain.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class DefaultUserAggregate implements UserAggregate {

    private final UserRepository userRepository;


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findOne(final Long id) throws UserNotFoundException, InvalidCriteriaException {
        if (Objects.isNull(id)) {
            throw new InvalidCriteriaException(String.format("User not found for criteria: '%d'", id));
        }
        return userRepository.findOne(id).orElseThrow(() -> new UserNotFoundException(String.format("User not found for criteria: '%d'", id)));
    }

    
    
}
