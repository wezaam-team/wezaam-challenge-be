package com.wezaam.withdrawal.infrastructure.config;

import com.wezaam.withdrawal.application.event.EventPublisher;
import com.wezaam.withdrawal.infrastructure.RabbitMQEventPublisher;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String WITHDRAWAL_CREATED_QUEUE = "withdrawalCreatedQueue";

    public static final String WITHDRAWAL_PROCESSED_QUEUE = "withdrawalProcessedQueue";

    @Bean
    public RabbitTemplate jsonRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonConverter());
        return template;
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue withdrawalCreatedQueue() {
        return new Queue(WITHDRAWAL_CREATED_QUEUE);
    }

    @Bean
    public Queue withdrawalProcessedQueue() {
        return new Queue(WITHDRAWAL_PROCESSED_QUEUE);
    }

    @Bean
    public EventPublisher eventPublisher(RabbitTemplate rabbitTemplate) {
        return new RabbitMQEventPublisher(rabbitTemplate);
    }

}