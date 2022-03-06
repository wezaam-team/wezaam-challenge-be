package com.weezam.challenge.user.adapter.out.persistence;

import com.weezam.challenge.user.adapter.out.persistence.entity.UserEntity;
import com.weezam.challenge.user.adapter.out.persistence.mapper.UserEntityMapper;
import com.weezam.challenge.user.domain.exception.InvalidCriteriaException;
import com.weezam.challenge.user.domain.exception.UserNotFoundException;
import com.weezam.challenge.user.domain.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DbUserRepositoryTest {

    @InjectMocks
    DbUserRepository repository;

    @Mock
    UserRepositoryJpa jpaRepository;

    @Mock
    UserEntityMapper mapper;

    @Test
    public void testFindAllUsers() {
        List<User> resExpected = new ArrayList<>();
        List<UserEntity> resEntity = new ArrayList<>();

        Mockito.when(jpaRepository.findAll()).thenReturn(resEntity);
        Mockito.when(mapper.toDomain(Mockito.anyList())).thenReturn(resExpected);

        List<User> res = repository.findAll();

        Assertions.assertNotNull(res);

        Mockito.verify(jpaRepository, Mockito.times(1)).findAll();
        Mockito.verify(mapper, Mockito.times(1)).toDomain(Mockito.anyList());
    }

    @Test
    public void testFindOne() throws UserNotFoundException, InvalidCriteriaException {
        User user = new User(1L, "user", null, 1000.0);
        UserEntity entity = new UserEntity(1L, "user", null, 1000.0);
        Mockito.when(jpaRepository.getById(Mockito.anyLong())).thenReturn(entity);
        Mockito.when(mapper.toDomain(Mockito.any(UserEntity.class))).thenReturn(user);

        Optional<User> res = repository.findOne(Long.valueOf(1l));
        Assertions.assertTrue(res.isPresent());
        Assertions.assertEquals(user, res.get());

        Mockito.verify(jpaRepository, Mockito.times(1)).getById(Mockito.anyLong());
        Mockito.verify(mapper, Mockito.times(1)).toDomain(Mockito.any(UserEntity.class));
    }
}
