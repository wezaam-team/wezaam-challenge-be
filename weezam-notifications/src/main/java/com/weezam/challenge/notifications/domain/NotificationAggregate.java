package com.weezam.challenge.notifications.domain;


import com.weezam.challenge.notifications.domain.exception.SendNotificationException;
import com.weezam.challenge.notifications.domain.model.NotificationData;

public interface NotificationAggregate {

    public void sendNotification(final NotificationData data) throws SendNotificationException;

}
