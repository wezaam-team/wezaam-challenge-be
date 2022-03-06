package com.weezam.challenge.user;

import com.weezam.challenge.user.domain.UserAggregate;
import com.weezam.challenge.user.domain.aggregate.DefaultUserAggregate;
import com.weezam.challenge.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserAggregationTest {

    @InjectMocks
    DefaultUserAggregate aggregate;

    @Mock
    UserRepository repository;

    @Test
    public void testFindAllUsers() {

    }

    @Test
    public void testFindOne() {

    }


}
