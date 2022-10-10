package com.wezaam.common.domain.model;

public interface DomainEventSubscriber<T> {

    void handleEvent(final T domainEvent);

    Class<T> subscribedToEventType();
}
