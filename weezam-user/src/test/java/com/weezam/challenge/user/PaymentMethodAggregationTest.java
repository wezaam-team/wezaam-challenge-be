package com.weezam.challenge.user;

import com.weezam.challenge.user.domain.aggregate.DefaultPaymentMethodAggregate;
import com.weezam.challenge.user.domain.aggregate.DefaultUserAggregate;
import com.weezam.challenge.user.domain.repository.PaymentMethodRepository;
import com.weezam.challenge.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodAggregationTest {

    @InjectMocks
    DefaultPaymentMethodAggregate aggregate;

    @Mock
    PaymentMethodRepository repository;

    @Test
    public void testFindAllUsers() {

    }

    @Test
    public void testFindOne() {

    }


}
