package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.PaymentMethod;

public interface WithdrawalProcessingService {

    Long sendToProcessing(Double amount, PaymentMethod paymentMethod) throws TransactionException;
}
