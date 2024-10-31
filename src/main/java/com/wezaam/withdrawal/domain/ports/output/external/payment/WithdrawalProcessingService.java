package com.wezaam.withdrawal.domain.ports.output.external.payment;

import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.domain.exception.TransactionException;

public interface WithdrawalProcessingService {

    Long sendToProcessing(Double amount, PaymentMethod paymentMethod) throws TransactionException;
}
