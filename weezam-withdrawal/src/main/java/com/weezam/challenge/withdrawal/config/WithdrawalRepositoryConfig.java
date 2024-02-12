package com.weezam.challenge.withdrawal.config;

import com.weezam.challenge.withdrawal.adapter.out.persistence.DbWithdrawalRepository;
import com.weezam.challenge.withdrawal.adapter.out.persistence.WithdrawalRepositoryJpa;
import com.weezam.challenge.withdrawal.adapter.out.persistence.mapper.WithdrawalEntityMapper;
import com.weezam.challenge.withdrawal.domain.repository.WithdrawalRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WithdrawalRepositoryConfig {

    @Bean
    public WithdrawalRepository userRepository(final WithdrawalRepositoryJpa repositoryJpa, final WithdrawalEntityMapper entityMapper) {
        return new DbWithdrawalRepository(repositoryJpa, entityMapper);
    }

}
