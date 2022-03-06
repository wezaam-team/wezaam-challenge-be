package com.weezam.challenge.withdrawal.config;

import com.weezam.challenge.withdrawal.domain.WithdrawalAggregate;
import com.weezam.challenge.withdrawal.domain.aggregate.DefaultWithdrawalAggregate;
import com.weezam.challenge.withdrawal.domain.clients.PaymentMethodClient;
import com.weezam.challenge.withdrawal.domain.clients.UserClient;
import com.weezam.challenge.withdrawal.domain.events.WithdrawalEventPublisher;
import com.weezam.challenge.withdrawal.domain.repository.WithdrawalRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WithdrawalAggregateConfig {

    @Bean
    public WithdrawalAggregate userAggregate(final WithdrawalRepository repository, final WithdrawalEventPublisher publisher, final UserClient userClient, final PaymentMethodClient paymentMethodClient) {
        return new DefaultWithdrawalAggregate(repository, publisher, userClient, paymentMethodClient);
    }
}
