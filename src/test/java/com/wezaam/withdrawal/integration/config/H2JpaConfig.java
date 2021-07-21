package com.wezaam.withdrawal.integration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.wezaam.withdrawal.repository")
@PropertySource("test-application.properties")
@EnableTransactionManagement
public class H2JpaConfig {

}