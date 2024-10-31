package com.weezam.challenge.withdrawal.adapter.out.amqp;

import com.weezam.challenge.withdrawal.config.WithdrawalCreatedQueueConfig;
import com.weezam.challenge.withdrawal.config.WithdrawalNotificationQueueConfig;
import com.weezam.challenge.withdrawal.domain.events.WithdrawalEvent;
import com.weezam.challenge.withdrawal.domain.events.WithdrawalEventPublisher;
import com.weezam.challenge.withdrawal.domain.model.Withdrawal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Slf4j
public class AmqpWithdrawalEventPublisher implements WithdrawalEventPublisher {

    private final AmqpTemplate amqpTemplate;

    @Override
    public void publishCreatedEvent(WithdrawalEvent event) {
        Withdrawal data = event.getWithdrawal();
        if (data.getExecuteAt() != null) {
            Instant nowTime = Instant.now();
            final long delayTimes = nowTime.until(data.getExecuteAt(), ChronoUnit.MILLIS);
            amqpTemplate.convertAndSend(WithdrawalCreatedQueueConfig.NEW_WITHDRAWAL_EXCHANGE_DELAYED, WithdrawalCreatedQueueConfig.NEW_WITHDRAWAL_QUEUE, event.getWithdrawal().getId(), new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setHeader("x-delay",delayTimes);
                    return message;
                }
            });
            return;
        }

        amqpTemplate.convertAndSend(WithdrawalCreatedQueueConfig.NEW_WITHDRAWAL_EXCHANGE, WithdrawalCreatedQueueConfig.NEW_WITHDRAWAL_QUEUE, event.getWithdrawal().getId());
    }

    @Override
    public void publishNotificationEvent(WithdrawalEvent event) {
        amqpTemplate.convertAndSend(WithdrawalNotificationQueueConfig.NOTIFICATION_EXCHANGE, WithdrawalNotificationQueueConfig.NOTIFICATION_QUEUE, event.getWithdrawal().getId());
    }
}
