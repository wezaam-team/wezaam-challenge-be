package com.wezaam.withdrawal.repository;

import com.wezaam.withdrawal.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testCreateAndReadUser() {
        User user = new User();
        user.setFirstName("Peter");
        user.setMaxWithdrawalAmount(1850.50d);

        userRepository.save(user);

        Optional<User> userEntity = userRepository.findByFirstName("Peter");
        Assertions.assertThat(userEntity.get()).extracting(User::getFirstName).isEqualTo("Peter");
        Assertions.assertThat(userEntity.get()).extracting(User::getMaxWithdrawalAmount).isEqualTo(1850.50d);
        Assertions.assertThat(userEntity.get().getUserPaymentMethods()).isNull();
    }

    @Test
    public void testReadAndDeleteUser() {
        Optional<User> userEntity = userRepository.findByFirstName("Arnold");
        Assertions.assertThat(userEntity.get()).extracting(User::getFirstName).isEqualTo("Arnold");
        Assertions.assertThat(userEntity.get()).extracting(User::getMaxWithdrawalAmount).isEqualTo(2000d);
        Assertions.assertThat(userEntity.get().getUserPaymentMethods()).isNotEmpty();

        userRepository.delete(userEntity.get());
        Assertions.assertThat(userRepository.findByFirstName("Arnold").isPresent()).isFalse();
    }

    @Test
    public void testUsersSize() {
        List<User> users = userRepository.findAll();
        Assertions.assertThat(users).hasSize(4);
    }


}
