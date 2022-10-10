package com.wezaam.withdrawal.port.adapter.service;

import com.wezaam.withdrawal.domain.model.PaymentMethod;
import com.wezaam.withdrawal.domain.model.TransactionException;
import com.wezaam.withdrawal.domain.service.WithdrawalProcessingService;
import org.springframework.stereotype.Service;

@Service
public class SimulatedWithdrawalProcessingService implements WithdrawalProcessingService {

    @Override
    public long sendToProcessing(Double amount, PaymentMethod paymentMethod) throws TransactionException {
        // call a payment provider
        // in case a transaction can be process
        // it generates a transactionId and process the transaction async
        // otherwise it throws TransactionException
        return System.nanoTime();
    }
}
