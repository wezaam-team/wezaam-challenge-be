package com.wezaam.withdrawal.infrastructure.dataaccess.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wezaam.withdrawal.common.UtilTest;
import com.wezaam.withdrawal.infrastructure.dataaccess.entity.PaymentMethod;
import com.wezaam.withdrawal.infrastructure.dataaccess.entity.User;
import com.wezaam.withdrawal.infrastructure.dataaccess.mapper.UserDataAccessMapper;
import com.wezaam.withdrawal.infrastructure.dataaccess.repository.UserJpaRepository;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest extends UtilTest {

    @InjectMocks UserRepositoryImpl underTest;
    @InjectMocks UserDataAccessMapper userDataAccessMapper;

    @Mock UserJpaRepository userJpaRepository;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(underTest, "userDataAccessMapper", userDataAccessMapper);
    }

    @Test
    void findByIdEmpty() {
        // arrange
        Long userId = 1L;
        when(userJpaRepository.findById(userId)).thenReturn(Optional.empty());
        // act
        Optional<com.wezaam.withdrawal.domain.entity.User> userOptional =
                underTest.findById(userId);

        // assert
        assertThat(userOptional).isEmpty();
        verify(userJpaRepository, times1).findById(anyLong());
    }

    @Test
    void findById() {
        // arrange
        Long userId = 1L;
        User user = getUser(userId);

        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(user));
        // act
        Optional<com.wezaam.withdrawal.domain.entity.User> userOptional =
                underTest.findById(userId);

        // assert
        assertThat(userOptional).isNotEmpty();
        com.wezaam.withdrawal.domain.entity.User userResponse = userOptional.get();
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getPaymentMethods()).isNotNull();
        assertThat(userResponse.getFirstName()).isNotNull();
        assertThat(userResponse.getMaxWithdrawalAmount()).isNotNull();

        verify(userJpaRepository, times1).findById(anyLong());
    }

    @Test
    void findAllEmpty() {
        // arrange
        when(userJpaRepository.findAll()).thenReturn(List.of());

        // act
        List<com.wezaam.withdrawal.domain.entity.User> userList = underTest.findAll();

        // assert
        assertThat(userList).isEmpty();
        verify(userJpaRepository, times1).findAll();
    }

    @Test
    void findAll() {
        // arrange
        when(userJpaRepository.findAll()).thenReturn(List.of(getUser(1L)));

        // act
        List<com.wezaam.withdrawal.domain.entity.User> userList = underTest.findAll();

        // assert
        assertThat(userList).isNotEmpty().hasSize(1);
        verify(userJpaRepository, times1).findAll();
        com.wezaam.withdrawal.domain.entity.User user = userList.getFirst();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getPaymentMethods()).isNotNull().hasSize(1);
        assertThat(user.getMaxWithdrawalAmount()).isNotNull();
        assertThat(user.getFirstName()).isNotNull();
        com.wezaam.withdrawal.domain.entity.PaymentMethod paymentMethod =
                user.getPaymentMethods().getFirst();
        assertThat(paymentMethod).isNotNull();
        assertThat(paymentMethod.getId()).isNotNull();
        assertThat(paymentMethod.getName()).isNotNull();
    }

    protected User getUser(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setFirstName("Lewis");
        user.setMaxWithdrawalAmount(400D);
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setName("Bank account");
        paymentMethod.setUser(user);
        user.setPaymentMethods(List.of(paymentMethod));
        return user;
    }
}
