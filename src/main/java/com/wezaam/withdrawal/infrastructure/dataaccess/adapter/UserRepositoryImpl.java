package com.wezaam.withdrawal.infrastructure.dataaccess.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.ports.output.repository.UserRepository;
import com.wezaam.withdrawal.infrastructure.dataaccess.entity.User;
import com.wezaam.withdrawal.infrastructure.dataaccess.mapper.UserDataAccessMapper;
import com.wezaam.withdrawal.infrastructure.dataaccess.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserDataAccessMapper userDataAccessMapper;

    @Override
    public Optional<com.wezaam.withdrawal.domain.entity.User> findById(Long userId) {
        return userJpaRepository.findById(userId).map(userDataAccessMapper::mapUserEntityAsUser);
    }

    @Override
    public List<com.wezaam.withdrawal.domain.entity.User> findAll() {
        List<User> userList = userJpaRepository.findAll();
        return userDataAccessMapper.mapUserListAsUserResponseList(userList);
    }
}
