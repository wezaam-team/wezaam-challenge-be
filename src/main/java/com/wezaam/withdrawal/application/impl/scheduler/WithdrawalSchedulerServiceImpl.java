package com.wezaam.withdrawal.application.impl.scheduler;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.application.WithdrawalProcessorService;
import com.wezaam.withdrawal.application.WithdrawalSchedulerService;
import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.event.WithdrawalEvent;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WithdrawalSchedulerServiceImpl implements WithdrawalSchedulerService {

    private final WithdrawalScheduledRepository withdrawalScheduledRepository;
    private final WithdrawalProcessorService withdrawalProcessorService;

    @Scheduled(fixedDelay = 5000)
    public void run() {
        withdrawalScheduledRepository
                .findAllByExecuteAtBeforeWithStatus(Instant.now(), WithdrawalStatus.PENDING)
                .forEach(this::processScheduled);
    }

    private void processScheduled(WithdrawalScheduled withdrawalScheduled) {
        WithdrawalEvent withdrawalEvent =
                WithdrawalEvent.builder()
                        .id(withdrawalScheduled.getId())
                        .withdrawalType(WithdrawalType.SCHEDULED)
                        .paymentMethodId(withdrawalScheduled.getPaymentMethodId())
                        .build();
        withdrawalProcessorService.process(withdrawalEvent);
    }
}
