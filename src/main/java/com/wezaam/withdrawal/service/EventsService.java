package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.domain.WithdrawalProcessingResponse;
import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


public interface EventsService {

    void sendWithdrawalStatusMessage(WithdrawalProcessingResponse processingResponse);

}
