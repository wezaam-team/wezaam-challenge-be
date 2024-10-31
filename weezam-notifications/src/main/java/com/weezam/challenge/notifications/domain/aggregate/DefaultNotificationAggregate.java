package com.weezam.challenge.notifications.domain.aggregate;

import com.weezam.challenge.notifications.domain.NotificationAggregate;
import com.weezam.challenge.notifications.domain.exception.SendNotificationException;
import com.weezam.challenge.notifications.domain.model.NotificationData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class DefaultNotificationAggregate implements NotificationAggregate {

    @Override
    public void sendNotification(NotificationData data) throws SendNotificationException {
        log.info("PROCESSING NOTIFICATIONS:: Message: {}", data.getData());
        throw new SendNotificationException("Error processing notifications");
    }
}
