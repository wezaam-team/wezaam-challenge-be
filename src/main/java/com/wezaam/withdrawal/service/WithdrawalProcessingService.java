package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public interface WithdrawalProcessingService {

    Long sendToProcessing(Double amount, PaymentMethod paymentMethod, User user) throws TransactionException;

}
