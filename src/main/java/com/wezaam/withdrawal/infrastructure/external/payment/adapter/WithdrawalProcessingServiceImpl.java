package com.wezaam.withdrawal.infrastructure.external.payment.adapter;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.domain.exception.TransactionException;
import com.wezaam.withdrawal.domain.ports.output.external.payment.WithdrawalProcessingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WithdrawalProcessingServiceImpl implements WithdrawalProcessingService {

    @Override
    public Long sendToProcessing(Double amount, PaymentMethod paymentMethod)
            throws TransactionException {
        log.info(
                "Start processing with amount={} and payment method id={}",
                amount,
                paymentMethod.getId());
        // call a payment provider
        // in case a transaction can be process
        // it generates a transactionId and process the transaction async
        // otherwise it throws TransactionException
        return System.nanoTime();
    }
}
