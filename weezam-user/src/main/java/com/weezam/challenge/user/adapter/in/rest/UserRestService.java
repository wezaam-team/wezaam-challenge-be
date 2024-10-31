package com.weezam.challenge.user.adapter.in.rest;

import java.util.List;
import java.util.stream.Collectors;

import com.weezam.challenge.user.adapter.in.rest.dto.UserDto;
import com.weezam.challenge.user.adapter.in.rest.mapper.UserMapper;
import com.weezam.challenge.user.domain.UserAggregate;
import com.weezam.challenge.user.domain.exception.InvalidCriteriaException;
import com.weezam.challenge.user.domain.exception.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/")
@Slf4j
@AllArgsConstructor
public class UserRestService {

    private final UserAggregate userAggregate;

    private final UserMapper userMapper;

    @GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserDto> findOne(final @PathVariable(value = "id") Long id) {
        try {
            UserDto res = userMapper.toDto(userAggregate.findOne(id));
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (UserNotFoundException ex) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found", ex);
        } catch (InvalidCriteriaException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found", ex);
        }
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> res = userAggregate.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    
}
