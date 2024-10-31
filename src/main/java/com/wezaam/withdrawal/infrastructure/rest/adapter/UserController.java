package com.wezaam.withdrawal.infrastructure.rest.adapter;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.wezaam.withdrawal.application.UserService;
import com.wezaam.withdrawal.domain.entity.User;
import com.wezaam.withdrawal.infrastructure.rest.dto.response.UserResponse;
import com.wezaam.withdrawal.infrastructure.rest.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserResponse> findAll() {
        log.info("Getting all user info");
        List<User> userList = userService.getAllUsers();
        return userMapper.mapUserListAsUserResponseList(userList);
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse findById(@PathVariable(value = "id") Long id) {
        log.info("Getting user info with id={}", id);
        User user = userService.getUserById(id);
        return userMapper.mapUserAsUserResponse(user);
    }
}
