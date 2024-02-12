package com.weezam.challenge.user.adapter.out.persistence;

import com.weezam.challenge.user.adapter.out.persistence.entity.PaymentMethodEntity;
import com.weezam.challenge.user.adapter.out.persistence.entity.UserEntity;
import com.weezam.challenge.user.adapter.out.persistence.mapper.PaymentMethodEntityMapper;
import com.weezam.challenge.user.domain.exception.InvalidCriteriaException;
import com.weezam.challenge.user.domain.exception.UserNotFoundException;
import com.weezam.challenge.user.domain.model.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DbPaymentMethodRepositoryTest {


    @InjectMocks
    DbPaymentMethodRepository repository;

    @Mock
    PaymentMethodRepositoryJpa jpaRepository;

    @Mock
    PaymentMethodEntityMapper mapper;

    @Test
    public void testFindOne() throws UserNotFoundException, InvalidCriteriaException {
        PaymentMethod payment = new PaymentMethod(1L, "user");
        PaymentMethodEntity entity = new PaymentMethodEntity(1L, new UserEntity(), null);
        Mockito.when(jpaRepository.getById(Mockito.anyLong())).thenReturn(entity);
        Mockito.when(mapper.toDomain(Mockito.any(PaymentMethodEntity.class))).thenReturn(payment);

        Optional<PaymentMethod> res = repository.findOne(Long.valueOf(1l));
        Assertions.assertTrue(res.isPresent());
        Assertions.assertEquals(payment, res.get());

        Mockito.verify(jpaRepository, Mockito.times(1)).getById(Mockito.anyLong());
        Mockito.verify(mapper, Mockito.times(1)).toDomain(Mockito.any(PaymentMethodEntity.class));
    }
}
