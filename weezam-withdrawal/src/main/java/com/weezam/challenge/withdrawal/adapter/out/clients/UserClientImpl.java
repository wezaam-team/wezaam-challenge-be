package com.weezam.challenge.withdrawal.adapter.out.clients;

import com.weezam.challenge.withdrawal.adapter.out.clients.dto.UserDto;
import com.weezam.challenge.withdrawal.adapter.out.clients.mapper.UserMapper;
import com.weezam.challenge.withdrawal.domain.clients.UserClient;
import com.weezam.challenge.withdrawal.domain.exception.UserNotFoundException;
import com.weezam.challenge.withdrawal.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class UserClientImpl implements UserClient {

    private final OpenFeignUserClient openFeignUserClient;
    private final UserMapper userMapper;

    @Override
    public Optional<User> findOne(Long id) throws UserNotFoundException {
        try {
            UserDto res = openFeignUserClient.findOne(id);
            return Optional.ofNullable(userMapper.toDomain(res));
        } catch (Exception exception) {
            log.error("Error getting User with id {}", id,  exception);
            throw new UserNotFoundException(String.format("User with id {} no found", id));
        }
    }
}
