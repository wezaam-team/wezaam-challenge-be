package com.wezaam.withdrawal.application.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wezaam.withdrawal.application.WithdrawalProcessorService;
import com.wezaam.withdrawal.application.WithdrawalService;
import com.wezaam.withdrawal.domain.command.CreateWithdrawalCommand;
import com.wezaam.withdrawal.domain.entity.User;
import com.wezaam.withdrawal.domain.entity.Withdrawal;
import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.event.WithdrawalEvent;
import com.wezaam.withdrawal.domain.exception.NotFoundException;
import com.wezaam.withdrawal.domain.ports.output.repository.UserRepository;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalRepository;
import com.wezaam.withdrawal.domain.ports.output.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;

    private final WithdrawalScheduledRepository withdrawalScheduledRepository;

    private final UserRepository userRepository;

    private final WithdrawalProcessorService withdrawalProcessorService;

    @Override
    @Transactional
    public Withdrawal create(CreateWithdrawalCommand createWithdrawalCommand) {

        User user =
                userRepository
                        .findById(createWithdrawalCommand.getUserId())
                        .orElseThrow(() -> new NotFoundException("User not found"));

        if (WithdrawalType.ASAP.name().equals(createWithdrawalCommand.getType().name())) {
            return processWithdrawal(createWithdrawalCommand, user);
        } else if (WithdrawalType.SCHEDULED
                .name()
                .equals(createWithdrawalCommand.getType().name())) {
            return processWithdrawalScheduled(createWithdrawalCommand, user);
        } else {
            throw new RuntimeException("Invalid withdrawal request");
        }
    }

    private Withdrawal processWithdrawal(
            CreateWithdrawalCommand createWithdrawalCommand, User user) {
        Withdrawal withdrawal =
                Withdrawal.builder()
                        .userId(createWithdrawalCommand.getUserId())
                        .paymentMethodId(createWithdrawalCommand.getPaymentMethodId())
                        .amount(createWithdrawalCommand.getAmount())
                        .createdAt(createWithdrawalCommand.getCreateAt())
                        .status(createWithdrawalCommand.getStatus())
                        .build();

        withdrawal.checkInitialWithdrawal(user.getMaxWithdrawalAmount(), user.getPaymentMethods());

        Withdrawal pendingWithdrawal = withdrawalRepository.save(withdrawal);
        publishingWithdrawal(pendingWithdrawal);
        return pendingWithdrawal;
    }

    private WithdrawalScheduled processWithdrawalScheduled(
            CreateWithdrawalCommand createWithdrawalCommand, User user) {
        WithdrawalScheduled withdrawalScheduled =
                WithdrawalScheduled.builder()
                        .userId(createWithdrawalCommand.getUserId())
                        .paymentMethodId(createWithdrawalCommand.getPaymentMethodId())
                        .amount(createWithdrawalCommand.getAmount())
                        .createdAt(createWithdrawalCommand.getCreateAt())
                        .status(createWithdrawalCommand.getStatus())
                        .executeAt(createWithdrawalCommand.getExecuteAt())
                        .build();
        withdrawalScheduled.checkInitialWithdrawal(
                user.getMaxWithdrawalAmount(), user.getPaymentMethods());

        return withdrawalScheduledRepository.save(withdrawalScheduled);
    }

    private void publishingWithdrawal(Withdrawal pendingWithdrawal) {

        WithdrawalEvent withdrawalMessage =
                WithdrawalEvent.builder()
                        .id(pendingWithdrawal.getId())
                        .withdrawalType(WithdrawalType.ASAP)
                        .paymentMethodId(pendingWithdrawal.getPaymentMethodId())
                        .build();
        withdrawalProcessorService.process(withdrawalMessage);
    }

    @Override
    public List<Withdrawal> getAllWithdrawal() {

        CompletableFuture<List<Withdrawal>> withdrawalsFuture =
                CompletableFuture.supplyAsync(this::getWithdrawals);
        CompletableFuture<List<WithdrawalScheduled>> scheduledWithdrawalsFuture =
                CompletableFuture.supplyAsync(this::getWithdrawalsScheduled);

        return CompletableFuture.allOf(withdrawalsFuture, scheduledWithdrawalsFuture)
                .thenApply(
                        ignored -> {
                            List<Withdrawal> combinedResult = new ArrayList<>();
                            combinedResult.addAll(withdrawalsFuture.join());
                            combinedResult.addAll(scheduledWithdrawalsFuture.join());
                            return combinedResult;
                        })
                .exceptionally(
                        ex -> {
                            log.error("Error fetching withdrawals", ex);
                            return Collections.emptyList();
                        })
                .join();
    }

    private List<Withdrawal> getWithdrawals() {
        return withdrawalRepository.findAll();
    }

    private List<WithdrawalScheduled> getWithdrawalsScheduled() {
        return withdrawalScheduledRepository.findAll();
    }
}
