package com.weezam.challenge.withdrawal.domain.events;

public interface WithdrawalEventPublisher {

    void publishCreatedEvent(final WithdrawalEvent event);

    void publishNotificationEvent(final WithdrawalEvent event);

}
