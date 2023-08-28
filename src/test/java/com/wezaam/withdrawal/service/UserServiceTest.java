package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	UserService userService;

	@Mock
	UserRepository userRepository;

	private User testUser1 = new User(1L, "test_name1", null, 100.0);
	private User testUser2 = new User(2L, "test_name2", null, 200.0);

	@Test
	void findById() {
		when(userRepository.findById(testUser1.getId())).thenReturn(Optional.ofNullable(testUser1));
		Assertions.assertEquals(testUser1, userService.findById(testUser1.getId()));
	}

	@Test
	void findAll() {
		List<User> testUsers = Arrays.asList(testUser1, testUser2);
		when(userRepository.findAll()).thenReturn(testUsers);
		Assertions.assertEquals(testUsers, userService.findAll());
	}
}