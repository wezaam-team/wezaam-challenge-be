package com.wezaam.withdrawal.service.impl;

import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.service.WithdrawalProcessingService;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalProcessingServiceImpl implements WithdrawalProcessingService {

    @Override
    public Long sendToProcessing(Double amount, PaymentMethod paymentMethod) throws TransactionException {
        // call a payment provider
        // in case a transaction can be process
        // it generates a transactionId and process the transaction async
        // otherwise it throws TransactionException
        return System.nanoTime();
    }
}
