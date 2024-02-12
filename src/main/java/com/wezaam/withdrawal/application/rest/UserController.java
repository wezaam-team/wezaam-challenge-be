package com.wezaam.withdrawal.application.rest;

import com.wezaam.withdrawal.application.rest.dto.GetUserResponseConverter;
import com.wezaam.withdrawal.application.rest.dto.GetUsersResponse;
import com.wezaam.withdrawal.domain.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Api
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<GetUsersResponse> getUsers() {
        return ResponseEntity.ok(
                new GetUsersResponse(
                        userService.getAll()
                                .stream()
                                .map(GetUserResponseConverter.aGetUsersResponseConverter()::apply)
                                .collect(Collectors.toList())
                )
        );

    }
}
