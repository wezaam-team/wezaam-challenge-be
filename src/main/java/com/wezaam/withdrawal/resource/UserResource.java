package com.wezaam.withdrawal.resource;

import com.wezaam.withdrawal.application.UserApplicationService;
import com.wezaam.withdrawal.application.representation.UserRepresentation;
import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Api
@RestController
public class UserResource {

    private final UserApplicationService userApplicationService;

    public UserResource(UserApplicationService userApplicationService) {
        if (userApplicationService == null) {
            throw new IllegalArgumentException("The userApplicationService should not be null.");
        }

        this.userApplicationService = userApplicationService;
    }

    @GetMapping("/users")
    public List<UserRepresentation> getAllUsers() {
        return this.userApplicationService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserRepresentation getUser(@PathVariable Long id) {
        return this.userApplicationService.getUser(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User of ID: " + id + " does not exist."));
    }
}
