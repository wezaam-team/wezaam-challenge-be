package com.weezam.challenge.notifications.adapter.in.amqp;

import com.weezam.challenge.notifications.config.NotificationEventConfig;
import com.weezam.challenge.notifications.domain.NotificationAggregate;
import com.weezam.challenge.notifications.domain.exception.SendNotificationException;
import com.weezam.challenge.notifications.domain.model.NotificationData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Component
public class AmqpNotificationListener {

    public static final String HEADER_X_RETRIES_COUNT = "x-retries-count";

    private final AmqpTemplate amqpTemplate;
    private final NotificationAggregate notificationAggregate;

    private static final int MAX_RETRY = 5;

    @RabbitListener(queues = NotificationEventConfig.NOTIFICATION_QUEUE)
    public void processMessages(Message message) throws SendNotificationException {
        NotificationData data = new NotificationData(message.toString());
        notificationAggregate.sendNotification(data);
    }

    @RabbitListener(queues = NotificationEventConfig.QUEUE_DLX)
    public void processFailedMessagesRetryWithParkingLot(Message failedMessage) {
        Integer retriesCnt = (Integer) failedMessage.getMessageProperties().getHeaders().get(HEADER_X_RETRIES_COUNT);
        if (Objects.isNull(retriesCnt)) {
            retriesCnt = 1;
        }
        if (retriesCnt > MAX_RETRY) {
            log.info("Sending message to the parking lot queue");
            amqpTemplate.send(NotificationEventConfig.EXCHANGE_PARKING_LOT, failedMessage.getMessageProperties().getReceivedRoutingKey(), failedMessage);
            return;
        }

        log.info("Retrying message for the {} time", retriesCnt);
        failedMessage.getMessageProperties().getHeaders().put(HEADER_X_RETRIES_COUNT, ++retriesCnt);
        amqpTemplate.send(NotificationEventConfig.NOTIFICATION_EXCHANGE, failedMessage.getMessageProperties().getReceivedRoutingKey(), failedMessage);
    }

    @RabbitListener(queues = NotificationEventConfig.QUEUE_PARKING_LOT)
    public void processParkingLotQueue(Message failedMessage) {
        log.info("Received message in parking lot queue {}", failedMessage.toString());
    }

}
