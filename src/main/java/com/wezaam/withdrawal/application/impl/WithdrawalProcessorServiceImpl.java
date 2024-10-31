package com.wezaam.withdrawal.application.impl;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.application.WithdrawalProcessorService;
import com.wezaam.withdrawal.application.impl.processor.WithdrawalTypeProcessor;
import com.wezaam.withdrawal.application.impl.processor.WithdrawalTypeStrategy;
import com.wezaam.withdrawal.domain.event.WithdrawalEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawalProcessorServiceImpl implements WithdrawalProcessorService {

    private final WithdrawalTypeStrategy withdrawalTypeStrategy;

    @Override
    public void process(WithdrawalEvent withdrawalEvent) {

        log.info(
                "Sending to process withdrawal type {} with id={} and payment method id={}",
                withdrawalEvent.getWithdrawalType(),
                withdrawalEvent.getId(),
                withdrawalEvent.getPaymentMethodId());

        WithdrawalTypeProcessor withdrawalTypeProcessor =
                withdrawalTypeStrategy.getWithdrawalProcessor(withdrawalEvent.getWithdrawalType());

        withdrawalTypeProcessor.execute(withdrawalEvent);
    }
}
