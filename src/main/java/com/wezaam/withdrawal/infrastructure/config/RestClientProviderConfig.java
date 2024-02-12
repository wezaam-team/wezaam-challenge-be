package com.wezaam.withdrawal.infrastructure.config;

import com.wezaam.withdrawal.infrastructure.RestClientProvider;
import com.wezaam.withdrawal.domain.Provider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientProviderConfig {

    @Bean
    public Provider provider() {
        return new RestClientProvider();
    }
}
