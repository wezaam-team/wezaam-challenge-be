package com.wezaam.withdrawal.rest;

import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.service.UserService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/find-all-users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/find-user-by-id/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User findById(@PathVariable Long userId) {
        return userService.findById(userId);
    }
}
