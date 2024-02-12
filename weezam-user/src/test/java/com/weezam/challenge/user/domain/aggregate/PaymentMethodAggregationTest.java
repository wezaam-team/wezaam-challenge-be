package com.weezam.challenge.user.domain.aggregate;

import com.weezam.challenge.user.domain.exception.InvalidCriteriaException;
import com.weezam.challenge.user.domain.exception.PaymentMethodNotFoundException;
import com.weezam.challenge.user.domain.model.PaymentMethod;
import com.weezam.challenge.user.domain.repository.PaymentMethodRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodAggregationTest {

    @InjectMocks
    DefaultPaymentMethodAggregate aggregate;

    @Mock
    PaymentMethodRepository repository;

    @Test
    public void testFindOne() throws InvalidCriteriaException, PaymentMethodNotFoundException {
        PaymentMethod payment = new PaymentMethod(1L, "user");
        Mockito.when(repository.findOne(Mockito.anyLong())).thenReturn(Optional.of(payment));
        PaymentMethod res = aggregate.findOne(Long.valueOf(1l));
        Assertions.assertNotNull(res);
        Assertions.assertEquals(payment, res);
        Mockito.verify(repository, Mockito.times(1)).findOne(Mockito.anyLong());
    }

    @Test
    public void testFindOne_InvalidCriteria() {

        Exception exception = Assertions.assertThrows(
                InvalidCriteriaException.class,
                () -> aggregate.findOne(null));

        Assertions.assertEquals("PaymentMethod not found. Null criteria", exception.getMessage());
    }

    @Test
    public void testFindOne_NotFound()  {
        Mockito.when(repository.findOne(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));

        Exception exception = Assertions.assertThrows(
                PaymentMethodNotFoundException.class,
                () -> aggregate.findOne(1L));

        Assertions.assertEquals("PaymentMethod not found for criteria: '1'", exception.getMessage());
    }


}
