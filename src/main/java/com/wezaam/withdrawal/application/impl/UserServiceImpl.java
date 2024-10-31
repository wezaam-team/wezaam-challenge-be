package com.wezaam.withdrawal.application.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wezaam.withdrawal.application.UserService;
import com.wezaam.withdrawal.domain.entity.User;
import com.wezaam.withdrawal.domain.exception.NotFoundException;
import com.wezaam.withdrawal.domain.ports.output.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User was not found with id=" + userId));
    }
}
