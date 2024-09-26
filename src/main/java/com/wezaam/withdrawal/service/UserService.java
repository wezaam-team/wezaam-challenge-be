package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User addUser(User user);
    void deleteUser(Long userId);
    Optional<User> getUser(Long userId);
    List<User> findAllUsers();
    User findByName(String name) throws Exception;
}
