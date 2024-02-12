package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.NotFoundException;
import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.wezaam.withdrawal.service.MocksHelper.mockUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void testFindAll() {
        userService.findAll();
        verify(userRepository).findAll();
    }

    @Test(expected = NotFoundException.class)
    public void testFindByIdWhenUserDoesNotExists() {
        Long userId = 1l;
        when(userRepository.findById(userId)).thenThrow(new NotFoundException(""));

        userService.findById(userId);
    }

    @Test
    public void testFindByIdWhenUserExists() {
        Double maxWithdrawalAmount = 100D;
        Long paymentMethodId = 1L;
        Long userId = 1L;
        User mockUser = mockUser(maxWithdrawalAmount,paymentMethodId);
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userService.findById(userId);
        assertEquals(userId, result.getId());
    }
}
