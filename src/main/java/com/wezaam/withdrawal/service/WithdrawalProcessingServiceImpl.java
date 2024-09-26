package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.User;
import org.springframework.stereotype.Service;

@Service
public class WithdrawalProcessingServiceImpl implements WithdrawalProcessingService {

    /**
     * This method can has a call to a factory to generate a new Provider Impl in function of paymentMethod
     * and apply the strategyPattern for example.
     * @param amount
     * @param paymentMethod
     * @param user
     * @return Long transactionId
     * @throws TransactionException
     */
    public Long sendToProcessing(Double amount, PaymentMethod paymentMethod, User user) throws TransactionException {
        // call a payment provider
        // in case a transaction can be process
        // it generates a transactionId and process the transaction async
        // otherwise it throws TransactionException

        return System.nanoTime();
    }
}
