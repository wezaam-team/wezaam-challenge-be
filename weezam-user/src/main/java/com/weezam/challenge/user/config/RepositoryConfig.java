package com.weezam.challenge.user.config;

import com.weezam.challenge.user.adapter.out.persistence.DbPaymentMethodRepository;
import com.weezam.challenge.user.adapter.out.persistence.DbUserRepository;
import com.weezam.challenge.user.adapter.out.persistence.PaymentMethodRepositoryJpa;
import com.weezam.challenge.user.adapter.out.persistence.UserRepositoryJpa;
import com.weezam.challenge.user.adapter.out.persistence.mapper.PaymentMethodEntityMapper;
import com.weezam.challenge.user.adapter.out.persistence.mapper.UserEntityMapper;
import com.weezam.challenge.user.domain.repository.PaymentMethodRepository;
import com.weezam.challenge.user.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public UserRepository userRepository(final UserRepositoryJpa repository, final UserEntityMapper mapper) {
        return new DbUserRepository(repository, mapper);
    }

    @Bean
    public PaymentMethodRepository paymentMethodRepository(final PaymentMethodRepositoryJpa repository, final PaymentMethodEntityMapper mapper) {
        return new DbPaymentMethodRepository(repository, mapper);
    }

}
