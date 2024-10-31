package com.wezaam.withdrawal.infrastructure.external.publisher.kafka.adapter;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.wezaam.withdrawal.domain.entity.Withdrawal;
import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.ports.output.external.kafka.EventsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WithdrawalEventPublisher implements EventsService {

    @Async
    @Override
    public void send(Withdrawal withdrawal) {
        log.info("Sending withdrawal event id={}", withdrawal.getId());
        // build and send an event in message queue async
    }

    @Async
    @Override
    public void send(WithdrawalScheduled withdrawalScheduled) {
        log.info("Sending withdrawalScheduled event id={}", withdrawalScheduled.getId());
        // build and send an event in message queue async
    }
}
