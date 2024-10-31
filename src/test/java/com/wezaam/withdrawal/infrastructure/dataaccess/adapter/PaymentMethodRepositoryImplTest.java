package com.wezaam.withdrawal.infrastructure.dataaccess.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wezaam.withdrawal.common.UtilTest;
import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.infrastructure.dataaccess.entity.User;
import com.wezaam.withdrawal.infrastructure.dataaccess.mapper.PaymentMethodDataAccessMapper;
import com.wezaam.withdrawal.infrastructure.dataaccess.repository.PaymentMethodJpaRepository;

@ExtendWith(MockitoExtension.class)
class PaymentMethodRepositoryImplTest extends UtilTest {

    @InjectMocks PaymentMethodRepositoryImpl underTest;
    @InjectMocks PaymentMethodDataAccessMapper paymentMethodDataAccessMapper;

    @Mock PaymentMethodJpaRepository paymentMethodJpaRepository;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(
                underTest, "paymentMethodDataAccessMapper", paymentMethodDataAccessMapper);
    }

    @Test
    void shouldGetPaymentMethodByIdEmpty() {

        // arrange
        Long paymentMethodId = 1L;
        when(paymentMethodJpaRepository.findById(paymentMethodId)).thenReturn(Optional.empty());

        // act
        Optional<PaymentMethod> paymentMethod = underTest.findById(paymentMethodId);

        // assert
        assertThat(paymentMethod).isEmpty();
        verify(paymentMethodJpaRepository, times1).findById(anyLong());
    }

    @Test
    void shouldGetPaymentMethodById() {

        // arrange
        Long paymentMethodId = 1L;

        com.wezaam.withdrawal.infrastructure.dataaccess.entity.PaymentMethod response =
                new com.wezaam.withdrawal.infrastructure.dataaccess.entity.PaymentMethod();
        response.setId(1L);
        response.setName("Bank account");
        response.setUser(new User());

        when(paymentMethodJpaRepository.findById(paymentMethodId))
                .thenReturn(Optional.of(response));

        // act
        Optional<PaymentMethod> paymentMethod = underTest.findById(paymentMethodId);

        // assert
        assertThat(paymentMethod).isNotEmpty();
        PaymentMethod paymentMethod1 = paymentMethod.get();
        assertThat(paymentMethod1).isNotNull();
        assertThat(paymentMethod1.getId()).isNotNull();
        assertThat(paymentMethod1.getName()).isNotNull();
        verify(paymentMethodJpaRepository, times1).findById(anyLong());
    }
}
