package com.wezaam.withdrawal.application.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.domain.entity.User;
import com.wezaam.withdrawal.domain.exception.NotFoundException;
import com.wezaam.withdrawal.domain.ports.output.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks UserServiceImpl underTest;

    @Mock UserRepository userRepository;

    @BeforeEach
    public void init() {}

    @Test
    void shouldGetAllUsersEmpty() {

        // arrange
        when(userRepository.findAll()).thenReturn(List.of());

        // act
        List<User> allUsers = underTest.getAllUsers();

        // assert
        assertThat(allUsers).isEmpty();
    }

    @Test
    void shouldGetAllUsers() {

        // arrange
        User user = getUser();
        when(userRepository.findAll()).thenReturn(List.of(user));
        // act
        List<User> allUsers = underTest.getAllUsers();

        // assert
        assertThat(allUsers).isNotNull().hasSize(1);
        User userResponse = allUsers.getFirst();
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getFirstName()).isNotNull();
        assertThat(userResponse.getMaxWithdrawalAmount()).isNotNull();
        assertThat(userResponse.getPaymentMethods()).isNotNull();
        PaymentMethod paymentMethodResponse = userResponse.getPaymentMethods().getFirst();
        assertThat(paymentMethodResponse.getId())
                .isNotNull()
                .isEqualTo(user.getPaymentMethods().getFirst().getId());
        assertThat(paymentMethodResponse.getName())
                .isNotNull()
                .isEqualTo(user.getPaymentMethods().getFirst().getName());
        assertThat(paymentMethodResponse.getId())
                .isNotNull()
                .isEqualTo(user.getPaymentMethods().getFirst().getId());
        assertThat(paymentMethodResponse.getName())
                .isNotNull()
                .isEqualTo(user.getPaymentMethods().getFirst().getName());
    }

    @Test
    void shouldGetUsersByIdNotFoundException() {

        // arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // act
        NotFoundException exception =
                assertThrows(NotFoundException.class, () -> underTest.getUserById(userId));

        // assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("User was not found with id=1");
    }

    @Test
    void shouldGetUsersById() {

        // arrange
        User user = getUser();
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        // act
        User userResponse = underTest.getUserById(userId);

        // assert
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getFirstName()).isNotNull();
        assertThat(userResponse.getMaxWithdrawalAmount()).isNotNull();
        assertThat(userResponse.getPaymentMethods()).isNotNull();
        PaymentMethod paymentMethodResponse = userResponse.getPaymentMethods().getFirst();
        assertThat(paymentMethodResponse).isNotNull();
        assertThat(paymentMethodResponse.getId())
                .isNotNull()
                .isEqualTo(user.getPaymentMethods().getFirst().getId());
        assertThat(paymentMethodResponse.getName())
                .isNotNull()
                .isEqualTo(user.getPaymentMethods().getFirst().getName());
    }

    protected User getUser() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setName("Bank account");
        return User.builder()
                .id(1L)
                .firstName("Lewis")
                .paymentMethods(List.of(paymentMethod))
                .maxWithdrawalAmount(5_000D)
                .build();
    }
}
