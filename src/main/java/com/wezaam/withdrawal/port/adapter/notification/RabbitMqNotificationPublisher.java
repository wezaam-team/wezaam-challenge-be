package com.wezaam.withdrawal.port.adapter.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wezaam.common.domain.model.DomainEvent;
import com.wezaam.common.domain.model.event.EventStore;
import com.wezaam.common.domain.model.event.StoredEvent;
import com.wezaam.common.domain.model.notification.Notification;
import com.wezaam.common.domain.model.notification.NotificationPublisher;
import com.wezaam.common.domain.model.notification.PublishedNotificationTracker;
import com.wezaam.common.domain.model.notification.PublishedNotificationTrackerStore;
import com.wezaam.common.port.adapter.messaging.Exchanges;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqNotificationPublisher implements NotificationPublisher {

    private final PublishedNotificationTrackerStore publishedNotificationTrackerStore;
    private final EventStore eventStore;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqNotificationPublisher(
            PublishedNotificationTrackerStore publishedNotificationTrackerStore,
            EventStore eventStore,
            ObjectMapper objectMapper,
            RabbitTemplate rabbitTemplate) {

        if (publishedNotificationTrackerStore == null) {
            throw new IllegalArgumentException("The publishedNotificationTrackerStore should not be null.");
        }

        if (eventStore == null) {
            throw new IllegalArgumentException("The eventStore should not be null.");
        }

        if (objectMapper == null) {
            throw new IllegalArgumentException("The objectMapper should not be null.");
        }

        if (rabbitTemplate == null) {
            throw new IllegalArgumentException("The rabbitTemplate should not be null.");
        }

        this.publishedNotificationTrackerStore = publishedNotificationTrackerStore;
        this.eventStore = eventStore;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishNotifications() {
        var publishedNotificationTracker = this.publishedNotificationTrackerStore
                .getPublishedNotificationTrackerOfTypeName(Exchanges.WITHDRAWAL_EXCHANGE_NAME);

        if (publishedNotificationTracker == null) {
            publishedNotificationTracker = new PublishedNotificationTracker(Exchanges.WITHDRAWAL_EXCHANGE_NAME);
        }

        List<StoredEvent> eventsToBePublished;

        if (publishedNotificationTracker.getMostRecentlyPublishedNotificationId() == null) {
            eventsToBePublished = this.eventStore.getAllStoredEvents();
        } else {
            eventsToBePublished = this.eventStore
                    .getAllStoredEventsSince(publishedNotificationTracker.getMostRecentlyPublishedNotificationId());
        }

        var notifications = eventsToBePublished
                .stream()
                .map(this::createNotificationFromStoredEvent)
                .collect(Collectors.toList());

        if (!notifications.isEmpty()) {

            var notificationsPublishedSuccessfully = this.rabbitTemplate.invoke(operations -> {
                notifications.forEach(notification -> operations.convertAndSend(
                        Exchanges.WITHDRAWAL_EXCHANGE_NAME, notification.getTypeName(), notification));

                operations.waitForConfirmsOrDie(10_000);

                return true;
            });

            if (Boolean.TRUE.equals(notificationsPublishedSuccessfully)) {
                var lastPublishedNotificationId = notifications.get(notifications.size() - 1).getId();

                publishedNotificationTracker.setMostRecentlyPublishedNotificationId(lastPublishedNotificationId);

                this.publishedNotificationTrackerStore.save(publishedNotificationTracker);
            }
        }
    }

    private <T extends DomainEvent> Notification<T> createNotificationFromStoredEvent(StoredEvent storedEvent) {
        T domainEvent = storedEvent.toDomainEvent(this.objectMapper);
        return new Notification<>(storedEvent.getId(), domainEvent);
    }
}
