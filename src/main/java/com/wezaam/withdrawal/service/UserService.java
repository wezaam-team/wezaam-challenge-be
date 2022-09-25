package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.NotFoundException;
import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with the id:" + id));
    }
}
