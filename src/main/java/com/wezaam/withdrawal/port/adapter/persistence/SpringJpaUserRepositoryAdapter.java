package com.wezaam.withdrawal.port.adapter.persistence;

import com.wezaam.withdrawal.domain.model.User;
import com.wezaam.withdrawal.domain.model.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SpringJpaUserRepositoryAdapter implements UserRepository {

    private final SpringJpaUserRepository springJpaUserRepository;

    public SpringJpaUserRepositoryAdapter(SpringJpaUserRepository springJpaUserRepository) {
        if (springJpaUserRepository == null) {
            throw new IllegalArgumentException("The springJpaUserRepository should not be null.");
        }

        this.springJpaUserRepository = springJpaUserRepository;
    }

    @Override
    public Optional<User> getUserOfId(long id) {
        return this.springJpaUserRepository.findById(id);
    }

    @Override
    public List<User> getAll() {
        return this.springJpaUserRepository.findAll();
    }
}
