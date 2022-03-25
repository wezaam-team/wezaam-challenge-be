package com.wezaam.withdrawal.rest;

import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.repository.UserRepository;
import com.wezaam.withdrawal.service.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserService userService;

    @GetMapping("/find-all-users")
    public List<User> findAll() {
        return userService.findAllUsers();
    }

    @GetMapping("/find-user-by-id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> optUser = userService.getUser(id);
        if (optUser.isPresent()) {
            return new ResponseEntity<User>(optUser.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/add-new-user",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping(value = "/delete-user/{id}")
    public Boolean deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Boolean.TRUE;
    }
}
