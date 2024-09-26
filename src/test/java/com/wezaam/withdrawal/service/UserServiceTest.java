package com.wezaam.withdrawal.service;


import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Test
    public void testFindByName() throws Exception {
        User user = new User();
        user.setFirstName("Peter");
        user.setMaxWithdrawalAmount(1850.50d);

        when(userRepository.findByFirstName("Peter")).thenReturn(Optional.of(user));

        //test
        User user1 = userService.findByName("Peter");
        verify(userRepository, times(1)).findByFirstName("Peter");
    }
}
