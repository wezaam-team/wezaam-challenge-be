package com.wezaam.withdrawal.application;

import com.wezaam.common.domain.model.DomainEvent;
import com.wezaam.common.domain.model.DomainEventPublisher;
import com.wezaam.common.domain.model.DomainEventSubscriber;
import com.wezaam.common.domain.model.event.EventStore;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DomainEventStorer implements DomainEventSubscriber<DomainEvent> {

    private final EventStore eventStore;

    public DomainEventStorer(EventStore eventStore) {
        if (eventStore == null) {
            throw new IllegalArgumentException("The eventStore should not be null.");
        }

        this.eventStore = eventStore;
    }

    @Pointcut("execution(public * *(..)) "
            + "&& within(com.wezaam.withdrawal.application.*ApplicationService) "
            + "&& @within(org.springframework.stereotype.Service) "
            + "&& @annotation(org.springframework.transaction.annotation.Transactional)")
    private void applicationOperation() {}

    @Before("applicationOperation()")
    public void subscribeToDomainEvents() {
        if (!DomainEventPublisher.instance().hasSubscriber(this)) {
            DomainEventPublisher
                    .instance()
                    .subscribe(this);
        }
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        this.store(domainEvent);
    }

    @Override
    public Class<DomainEvent> subscribedToEventType() {
        return DomainEvent.class; // all domain events
    }

    private void store(DomainEvent domainEvent) {
        this.eventStore.append(domainEvent);
    }
}
