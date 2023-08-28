package com.wezaam.withdrawal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.dto.TransactionRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalProcessingService {

    @Autowired
    private EventsService eventsService;


    public Long sendToProcessing(Double amount, PaymentMethod paymentMethod, Double maxWithdrawalAmount) throws TransactionException {
        // call a payment provider
        // in case a transaction can be process
        // it generates a transactionId and process the transaction async
        // otherwise it throws TransactionException
        if (amount > maxWithdrawalAmount) {
            throw new TransactionException("Payout amount exceeds withdrawal limit");
        }
        Long transactionId = System.nanoTime();
        try {
            eventsService.send(new TransactionRequestDto(amount, paymentMethod, transactionId));
        } catch (JsonProcessingException e) {
            throw new TransactionException("Internal error. Could not process request.");
        }
        return transactionId;
    }
}
