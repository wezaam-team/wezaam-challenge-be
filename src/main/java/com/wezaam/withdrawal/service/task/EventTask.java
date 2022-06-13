package com.wezaam.withdrawal.service.task;

import com.wezaam.withdrawal.model.Event;
import com.wezaam.withdrawal.repository.EventRepository;
import com.wezaam.withdrawal.service.EventsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventTask {

    private final EventsService eventsService;
    private final EventRepository eventRepository;

    public EventTask(EventsService eventsService, EventRepository eventRepository) {
        this.eventsService = eventsService;
        this.eventRepository = eventRepository;
    }

    @Scheduled(fixedDelay = 5000)
    public void run() {

        List<Event> allEvents = eventRepository.findAll();

        allEvents.stream().filter(event -> event.getWithdrawal() != null).forEach(event -> {
            eventsService.send(event.getWithdrawal());
            eventRepository.delete(event);
        });

        allEvents.stream().filter(event -> event.getWithdrawalScheduled() != null).forEach(event -> {
            eventsService.send(event.getWithdrawalScheduled());
            eventRepository.delete(event);
        });
    }

}
