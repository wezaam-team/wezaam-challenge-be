package com.weezam.challenge.user.config;

import com.weezam.challenge.user.domain.PaymentMethodAggregate;
import com.weezam.challenge.user.domain.UserAggregate;
import com.weezam.challenge.user.domain.aggregate.DefaultPaymentMethodAggregate;
import com.weezam.challenge.user.domain.aggregate.DefaultUserAggregate;
import com.weezam.challenge.user.domain.repository.PaymentMethodRepository;
import com.weezam.challenge.user.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AggregateConfig {

    @Bean
    public UserAggregate userAggregate(final UserRepository repository) {
        return new DefaultUserAggregate(repository);
    }

    @Bean
    public PaymentMethodAggregate paymentMethodAggregate(final PaymentMethodRepository repository) {
        return new DefaultPaymentMethodAggregate(repository);
    }
}
