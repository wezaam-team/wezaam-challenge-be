package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.domain.rest.request.WithdrawalListResponse;
import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.*;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.repository.WithdrawalRepository;
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public interface WithdrawalService {


    void create(Withdrawal withdrawal);
    void schedule(WithdrawalScheduled withdrawalScheduled);
    WithdrawalListResponse findAll();
}
