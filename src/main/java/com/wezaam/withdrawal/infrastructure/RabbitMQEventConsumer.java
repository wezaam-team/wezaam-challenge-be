package com.wezaam.withdrawal.infrastructure;

import com.wezaam.withdrawal.application.event.WithdrawalEventsConsumer;
import com.wezaam.withdrawal.domain.event.WithdrawalClosed;
import com.wezaam.withdrawal.domain.event.WithdrawalCreated;
import com.wezaam.withdrawal.domain.event.WithdrawalInvalidated;
import com.wezaam.withdrawal.domain.event.WithdrawalProcessed;
import com.wezaam.withdrawal.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQEventConsumer {

    @Autowired
    private WithdrawalEventsConsumer withdrawalEventsConsumer;

    @RabbitListener(queues = RabbitMQConfig.WITHDRAWAL_CREATED_QUEUE)
    public void onWithdrawalEvent(WithdrawalCreated event) throws IOException {
        withdrawalEventsConsumer.withdrawalCreated(event);
    }

    @RabbitListener(queues = RabbitMQConfig.WITHDRAWAL_PROCESSED_QUEUE)
    public void onWithdrawalProcessed(WithdrawalProcessed event) throws IOException {
        withdrawalEventsConsumer.withdrawalProcessed(event);
    }

    @RabbitListener(queues = RabbitMQConfig.WITHDRAWAL_CLOSED_QUEUE)
    public void onWithdrawalClosed(WithdrawalClosed event) throws IOException {
        withdrawalEventsConsumer.withdrawalClosed(event);
    }

    @RabbitListener(queues = RabbitMQConfig.WITHDRAWAL_INVALIDATE_QUEUE)
    public void onWithdrawalInvalidated(WithdrawalInvalidated event) throws IOException {
        withdrawalEventsConsumer.withdrawalInvalidated(event);
    }
}
