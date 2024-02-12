package com.weezam.challenge.user.domain.aggregate;

import com.weezam.challenge.user.domain.exception.InvalidCriteriaException;
import com.weezam.challenge.user.domain.exception.UserNotFoundException;
import com.weezam.challenge.user.domain.model.User;
import com.weezam.challenge.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserAggregationTest {

    @InjectMocks
    DefaultUserAggregate aggregate;

    @Mock
    UserRepository repository;

    @Test
    public void testFindAllUsers() {
        User user = new User(1L, "user", null, 1000.0);
        List<User> expectedRes = List.of(user);

        Mockito.when(repository.findAll()).thenReturn(expectedRes);
        List<User> res = aggregate.findAll();
        Assertions.assertNotNull(res);
        Assertions.assertFalse(res.isEmpty());
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    public void testFindOne() throws UserNotFoundException, InvalidCriteriaException {
        User user = new User(1L, "user", null, 1000.0);
        Mockito.when(repository.findOne(Mockito.anyLong())).thenReturn(Optional.of(user));
        User res = aggregate.findOne(Long.valueOf(1l));
        Assertions.assertNotNull(res);
        Assertions.assertEquals(user, res);
        Mockito.verify(repository, Mockito.times(1)).findOne(Mockito.anyLong());
    }

    @Test
    public void testFindOne_InvalidCriteria() throws UserNotFoundException, InvalidCriteriaException {

        Exception exception = Assertions.assertThrows(
                InvalidCriteriaException.class,
                () -> aggregate.findOne(null));

        Assertions.assertEquals("User not found. Null criteria", exception.getMessage());
    }

    @Test
    public void testFindOne_NotFound() throws UserNotFoundException, InvalidCriteriaException {
        Mockito.when(repository.findOne(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));

        Exception exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> aggregate.findOne(1L));

        Assertions.assertEquals("User not found for criteria: '1'", exception.getMessage());
    }


}
