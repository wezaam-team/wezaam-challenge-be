package com.wezaam.withdrawal.service.impl;

import com.wezaam.withdrawal.model.AbstractWithdrawal;
import com.wezaam.withdrawal.model.Event;
import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import com.wezaam.withdrawal.repository.EventRepository;
import com.wezaam.withdrawal.service.EventsService;
import com.wezaam.withdrawal.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class EventsServiceImpl implements EventsService {

    private final TestService testService;
    private final EventRepository eventRepository;

    public EventsServiceImpl(TestService testService,
                             EventRepository eventRepository) {
        this.testService = testService;
        this.eventRepository = eventRepository;
    }

    @Override
    public void send(Withdrawal withdrawal) {
        testService.test();
        // build and send an event in message queue async
    }

    @Override
    public void send(WithdrawalScheduled withdrawal) {
        // build and send an event in message queue async
    }

    @Override
    public void recover(RuntimeException e, AbstractWithdrawal withdrawal){
        Event event = new Event();
        if(withdrawal instanceof Withdrawal){
            event.setWithdrawal((Withdrawal) withdrawal);
        } else {
            event.setWithdrawalScheduled((WithdrawalScheduled) withdrawal);
        }

        eventRepository.saveAndFlush(event);
    }

}
