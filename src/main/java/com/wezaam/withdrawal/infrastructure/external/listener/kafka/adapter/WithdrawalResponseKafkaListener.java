package com.wezaam.withdrawal.infrastructure.external.listener.kafka.adapter;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.infrastructure.external.dto.WithdrawalMessageEmulate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WithdrawalResponseKafkaListener
        implements KafkaConsumerFake<WithdrawalMessageEmulate> {

    @Override
    public void receive(WithdrawalMessageEmulate withdrawalMessageEmulate) {
        log.info("Receive withdrawal from topic");
        // process event and save
    }
}
