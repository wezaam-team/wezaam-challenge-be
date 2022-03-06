package com.weezam.challenge.user.adapter.out.persistence;

import com.weezam.challenge.user.adapter.out.persistence.entity.UserEntity;
import com.weezam.challenge.user.adapter.out.persistence.mapper.UserEntityMapper;
import com.weezam.challenge.user.domain.model.User;
import com.weezam.challenge.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@AllArgsConstructor
public class DbUserRepository implements UserRepository {

    private final UserRepositoryJpa jpaUserRepository;

    private final UserEntityMapper userEntityMapper;

    @Override
    public List<User> findAll() {
        List<UserEntity> res = jpaUserRepository.findAll();
        return userEntityMapper.toDomain(res);
    }

    @Override
    public Optional<User> findOne(Long id) {
        UserEntity userEntity = jpaUserRepository.getById(id);
        return Optional.ofNullable(userEntityMapper.toDomain(userEntity));
    }
}
