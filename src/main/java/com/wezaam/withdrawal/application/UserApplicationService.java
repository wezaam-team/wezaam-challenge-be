package com.wezaam.withdrawal.application;

import com.wezaam.withdrawal.application.representation.UserRepresentation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wezaam.withdrawal.domain.model.UserRepository;

@Service
public class UserApplicationService {

    private final UserRepository userRepository;

    public UserApplicationService(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("The userRepository should not be null.");
        }

        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserRepresentation> getAllUsers() {
        return this.userRepository.getAll().stream().map(UserRepresentation::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserRepresentation> getUser(long userId) {
        return this.userRepository.getUserOfId(userId).map(UserRepresentation::new);
    }
}
