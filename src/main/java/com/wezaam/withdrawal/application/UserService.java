package com.wezaam.withdrawal.application;

import java.util.List;

import com.wezaam.withdrawal.domain.entity.User;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long userId);
}
