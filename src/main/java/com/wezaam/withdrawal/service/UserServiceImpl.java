package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.UserNotFoundException;
import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    @Override
    public User addUser(User user) {

        User entityUser = userRepository.save(user);

        //if (!user.getUserPaymentMethods().isEmpty()) {
        //    user.getUserPaymentMethods().forEach(p -> p.setUser(entityUser));
        //    paymentService.addPaymentMethods(user.getPaymentMethods());
        //}
        return entityUser;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByName(String name) throws UserNotFoundException {
        Optional<User> user = userRepository.findByFirstName(name);

        if (!user.isPresent()) throw new UserNotFoundException("User not found in the system");
        return user.get();
    }
}
