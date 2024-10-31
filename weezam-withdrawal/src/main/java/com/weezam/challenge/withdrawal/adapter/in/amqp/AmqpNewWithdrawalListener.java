package com.weezam.challenge.withdrawal.adapter.in.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weezam.challenge.withdrawal.config.WithdrawalCreatedQueueConfig;
import com.weezam.challenge.withdrawal.domain.WithdrawalAggregate;
import com.weezam.challenge.withdrawal.domain.exception.WithdrawalApplicationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Component
public class AmqpNewWithdrawalListener {

    private static final int MAX_RETRY = 5;
    private static final String HEADER_X_RETRIES_COUNT = "x-retries-count";

    private final AmqpTemplate amqpTemplate;
    private final WithdrawalAggregate withdrawalAggregate;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = WithdrawalCreatedQueueConfig.NEW_WITHDRAWAL_QUEUE)
    public void processMessages(Message message) throws WithdrawalApplicationException {
        try {
            Long withdrawalId = objectMapper.readValue(message.getBody(), Long.class);
            withdrawalAggregate.process(withdrawalId);
        } catch (Exception ex) {
            log.error("Error processing message from withdrawal queue {}", message);
            throw new WithdrawalApplicationException("Error processing message", ex);
        }

    }

    @RabbitListener(queues = WithdrawalCreatedQueueConfig.QUEUE_DLX)
    public void processFailedMessagesRetryWithParkingLot(Message failedMessage) {
        Integer retriesCnt = (Integer) failedMessage.getMessageProperties().getHeaders().get(HEADER_X_RETRIES_COUNT);
        if (Objects.isNull(retriesCnt)) {
            retriesCnt = 1;
        }

        if (retriesCnt > MAX_RETRY) {
            log.info("Sending message to the parking lot queue");
            amqpTemplate.send(WithdrawalCreatedQueueConfig.EXCHANGE_PARKING_LOT, failedMessage.getMessageProperties().getReceivedRoutingKey(), failedMessage);
            return;
        }

        log.info("Retrying message for the {} time", retriesCnt);
        failedMessage.getMessageProperties().getHeaders().put(HEADER_X_RETRIES_COUNT, ++retriesCnt);
        amqpTemplate.send(WithdrawalCreatedQueueConfig.NEW_WITHDRAWAL_EXCHANGE, failedMessage.getMessageProperties().getReceivedRoutingKey(), failedMessage);
    }

    @RabbitListener(queues = WithdrawalCreatedQueueConfig.QUEUE_PARKING_LOT)
    public void processParkingLotQueue(Message failedMessage) {
        log.info("Received message in parking lot queue {}", failedMessage.toString());
    }

}
