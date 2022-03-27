package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.domain.WithdrawalMessage;
import com.wezaam.withdrawal.domain.WithdrawalProcessingResponse;
import com.wezaam.withdrawal.event.WithdrawalEventPublisher;
import com.wezaam.withdrawal.model.WithdrawalExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private WithdrawalEventPublisher withdrawalEventPublisher;

    @Async
    public void sendWithdrawalStatusMessage(WithdrawalProcessingResponse processingResponse) {

        WithdrawalMessage message = new WithdrawalMessage();

        message.setPaymentMethodId(processingResponse.getPaymentMethod().getId());
        message.setUserId(processingResponse.getUser().getId());
        message.setTransactionId(processingResponse.getTransactionId());
        message.setMessage(String.format("The processing %s transaction %o with payment provider %s and user %s has obtained the status %s at datetime %s",
                    processingResponse.getExecution().name(),
                    processingResponse.getTransactionId(), processingResponse.getUser().getFirstName(),
                    processingResponse.getPaymentMethod().getName(),
                    processingResponse.getStatus().name(), processingResponse.getExecutedAt()));

        withdrawalEventPublisher.publishEvent(message);
    }
}
