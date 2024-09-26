package com.wezaam.withdrawal.domain.service;

import com.wezaam.withdrawal.domain.model.TransactionException;
import com.wezaam.withdrawal.domain.model.PaymentMethod;

public interface WithdrawalProcessingService {

    long sendToProcessing(Double amount, PaymentMethod paymentMethod) throws TransactionException;
}
