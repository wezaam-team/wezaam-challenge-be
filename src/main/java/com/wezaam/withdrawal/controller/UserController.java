package com.wezaam.withdrawal.controller;

import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/find-all-users")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/find-user-by-id/{id}")
    public User findById(@PathVariable Long id) { return userService.findById(id); }
}
