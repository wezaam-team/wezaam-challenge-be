package com.weezam.challenge.withdrawal.config;

import com.weezam.challenge.withdrawal.adapter.out.clients.OpenFeignPaymentMethodClient;
import com.weezam.challenge.withdrawal.adapter.out.clients.OpenFeignUserClient;
import com.weezam.challenge.withdrawal.adapter.out.clients.PaymentMethodClientImpl;
import com.weezam.challenge.withdrawal.adapter.out.clients.UserClientImpl;
import com.weezam.challenge.withdrawal.adapter.out.clients.mapper.PaymentMethodMapper;
import com.weezam.challenge.withdrawal.adapter.out.clients.mapper.UserMapper;
import com.weezam.challenge.withdrawal.domain.clients.PaymentMethodClient;
import com.weezam.challenge.withdrawal.domain.clients.UserClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WithdrawalClientConfig {

    @Bean
    public PaymentMethodClient paymentMethodClient(final OpenFeignPaymentMethodClient client, final PaymentMethodMapper mapper) {
        return new PaymentMethodClientImpl(client, mapper);
    }

    @Bean
    public UserClient userClient(final OpenFeignUserClient client, final UserMapper mapper) {
        return new UserClientImpl(client, mapper);
    }
}
