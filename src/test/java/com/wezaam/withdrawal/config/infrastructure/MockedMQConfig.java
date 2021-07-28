package com.wezaam.withdrawal.config.infrastructure;

import com.wezaam.withdrawal.application.event.EventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "test.rabbitMQConfig",
        name = "enabled",
        havingValue = "false")
public class MockedMQConfig {

    @Bean(MockedEventPublisher.BEAN_NAME)
    public EventPublisher eventPublisher(RabbitTemplate rabbitTemplate) {
        return new MockedEventPublisher();
    }
}
