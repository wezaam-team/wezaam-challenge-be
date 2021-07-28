package com.wezaam.withdrawal.config.infrastructure;

import com.wezaam.withdrawal.application.event.EventPublisher;
import com.wezaam.withdrawal.domain.event.WithdrawalCreated;
import com.wezaam.withdrawal.domain.event.WithdrawalEvent;
import com.wezaam.withdrawal.domain.event.WithdrawalProcessed;

import java.util.ArrayList;
import java.util.List;

public class MockedEventPublisher implements EventPublisher {

    public static final String BEAN_NAME = "eventPublisher";

    private List<Object> createdEvents;

    private List<Object> processedEvents;

    public MockedEventPublisher() {
        createdEvents = new ArrayList<>();
        processedEvents = new ArrayList<>();
    }

    @Override
    public void publish(WithdrawalEvent event) {
        if (event.getClass() == WithdrawalCreated.class) {
            createdEvents.add(event);
        } else if (event.getClass() == WithdrawalProcessed.class) {
            processedEvents.add(event);
        }
    }

    public List<Object> getCreatedEvents() {
        return new ArrayList<>(createdEvents);
    }

    public List<Object> getProcessedEvents() {
        return new ArrayList<>(processedEvents);
    }
}
