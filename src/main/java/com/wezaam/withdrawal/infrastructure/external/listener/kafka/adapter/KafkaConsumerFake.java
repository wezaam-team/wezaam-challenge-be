package com.wezaam.withdrawal.infrastructure.external.listener.kafka.adapter;

import com.wezaam.withdrawal.infrastructure.external.dto.WithdrawalMessageEmulate;

public interface KafkaConsumerFake<T> {

    void receive(WithdrawalMessageEmulate withdrawalMessageEmulate);
}
