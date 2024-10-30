package com.wezaam.withdrawal.domain.model;

import com.wezaam.common.domain.model.DomainEventSubscriber;
import java.util.ArrayList;
import java.util.List;

public class DomainEventCaptor<T> implements DomainEventSubscriber<T> {

    private Class<T> capturedEventType;
    private final List<T> capturedDomainEvents;

    public DomainEventCaptor(Class<T> capturedEventType) {
        this.setCapturedEventType(capturedEventType);

        this.capturedDomainEvents = new ArrayList<>();
    }

    @Override
    public void handleEvent(T domainEvent) {
        this.getCapturedDomainEvents().add(domainEvent);
    }

    @Override
    public Class<T> subscribedToEventType() {
        return this.getCapturedEventType();
    }

    public void reset() {
        this.getCapturedDomainEvents().clear();
    }

    public List<T> getCapturedDomainEvents() {
        return this.capturedDomainEvents;
    }

    protected void setCapturedEventType(Class<T> capturedEventType) {
        this.capturedEventType = capturedEventType;
    }

    public Class<T> getCapturedEventType() {
        return this.capturedEventType;
    }
}
