package com.weezam.challenge.withdrawal.adapter.out.amqp;

import com.weezam.challenge.withdrawal.config.WithdrawalCreatedQueueConfig;
import com.weezam.challenge.withdrawal.config.WithdrawalNotificationQueueConfig;
import com.weezam.challenge.withdrawal.domain.events.WithdrawalEvent;
import com.weezam.challenge.withdrawal.domain.events.WithdrawalEventPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;

@AllArgsConstructor
@Slf4j
public class AmqpWithdrawalEventPublisher implements WithdrawalEventPublisher {

    private final AmqpTemplate amqpTemplate;

    @Override
    public void publishCreatedEvent(WithdrawalEvent event) {
        amqpTemplate.convertAndSend(WithdrawalCreatedQueueConfig.NEW_WITHDRAWAL_EXCHANGE, WithdrawalCreatedQueueConfig.NEW_WITHDRAWAL_QUEUE, event.getWithdrawal().getId());
    }

    @Override
    public void publishNotificationEvent(WithdrawalEvent event) {
        amqpTemplate.convertAndSend(WithdrawalNotificationQueueConfig.NOTIFICATION_EXCHANGE, WithdrawalNotificationQueueConfig.NOTIFICATION_QUEUE, event.getWithdrawal().getId());
    }
}
