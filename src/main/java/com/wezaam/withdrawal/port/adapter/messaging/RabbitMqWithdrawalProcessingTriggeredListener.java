package com.wezaam.withdrawal.port.adapter.messaging;

import com.wezaam.common.domain.model.notification.Notification;
import com.wezaam.common.port.adapter.messaging.Exchanges;
import com.wezaam.withdrawal.domain.model.WithdrawalProcessingTriggered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqWithdrawalProcessingTriggeredListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqWithdrawalProcessingTriggeredListener.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "com.wezaam.withdrawal.port.adapter.messaging.RabbitMqWithdrawalProcessingTriggeredListener", durable = "true"),
            exchange = @Exchange(Exchanges.WITHDRAWAL_EXCHANGE_NAME),
            key = "com.wezaam.withdrawal.domain.model.WithdrawalProcessingTriggered"))
    public void logWithdrawalProcessingTriggeredNotification(
            Notification<WithdrawalProcessingTriggered> withdrawalProcessingTriggeredNotification) {
        logger.info(
                "Received a WithdrawalProcessingTriggered notification: " + withdrawalProcessingTriggeredNotification);
    }
}
