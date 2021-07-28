package com.wezaam.withdrawal.infrastructure;

import com.wezaam.withdrawal.application.event.EventPublisher;
import com.wezaam.withdrawal.domain.event.*;
import com.wezaam.withdrawal.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitMQEventPublisher implements EventPublisher {

    private static final Map<Class, String> MESSAGE_QUEUE_FOR_EVENT_TYPE =
            new HashMap<>();

    static {
        MESSAGE_QUEUE_FOR_EVENT_TYPE.put(WithdrawalCreated.class, RabbitMQConfig.WITHDRAWAL_CREATED_QUEUE);
        MESSAGE_QUEUE_FOR_EVENT_TYPE.put(WithdrawalProcessed.class, RabbitMQConfig.WITHDRAWAL_PROCESSED_QUEUE);
        MESSAGE_QUEUE_FOR_EVENT_TYPE.put(WithdrawalClosed.class, RabbitMQConfig.WITHDRAWAL_CLOSED_QUEUE);
        MESSAGE_QUEUE_FOR_EVENT_TYPE.put(WithdrawalInvalidated.class, RabbitMQConfig.WITHDRAWAL_INVALIDATE_QUEUE);
    }

    public RabbitMQEventPublisher() {
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(WithdrawalEvent event) {
        rabbitTemplate.convertAndSend(
                MESSAGE_QUEUE_FOR_EVENT_TYPE.get(event.getClass()),
                event
        );
    }
}
