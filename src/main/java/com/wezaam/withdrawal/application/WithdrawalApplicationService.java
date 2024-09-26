package com.wezaam.withdrawal.application;

import com.wezaam.withdrawal.application.representation.WithdrawalRepresentation;
import com.wezaam.withdrawal.domain.model.UserRepository;
import com.wezaam.withdrawal.domain.model.WithdrawalRepository;
import com.wezaam.withdrawal.domain.service.WithdrawalProcessingService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WithdrawalApplicationService {

    private final WithdrawalRepository withdrawalRepository;
    private final UserRepository userRepository;
    private final WithdrawalProcessingService withdrawalProcessingService;

    public WithdrawalApplicationService(
            WithdrawalRepository withdrawalRepository,
            UserRepository userRepository,
            WithdrawalProcessingService withdrawalProcessingService) {

        if (withdrawalRepository == null) {
            throw new IllegalArgumentException("The withdrawalRepository should not be null.");
        }

        if (userRepository == null) {
            throw new IllegalArgumentException("The userRepository should not be null.");
        }

        if (withdrawalProcessingService == null) {
            throw new IllegalArgumentException("The withdrawalProcessingService should not be null.");
        }

        this.withdrawalRepository = withdrawalRepository;
        this.userRepository = userRepository;
        this.withdrawalProcessingService = withdrawalProcessingService;
    }

    @Transactional
    public WithdrawalRepresentation createWithdrawal(
            long userId, long paymentMethodId, double amount, Instant executeAt) {

        var user = this.userRepository.getUserOfId(userId).orElseThrow(
                () -> new IllegalArgumentException("User of ID: " + userId + " does not exist."));

        var withdrawalId = this.withdrawalRepository.getNextId();
        var withdrawal = user.createWithdrawal(withdrawalId, paymentMethodId, amount, executeAt);

        if (withdrawal.isImmediate()) {
            withdrawal.triggerProcessing(withdrawalProcessingService, userRepository);
        }

        this.withdrawalRepository.save(withdrawal);

        return new WithdrawalRepresentation(withdrawal);
    }

    @Transactional
    public WithdrawalRepresentation triggerWithdrawalProcessing(long withdrawalId) {
        var withdrawal = this.withdrawalRepository.getWithdrawalOfId(withdrawalId).orElseThrow(
                () -> new IllegalArgumentException("Withdrawal of ID: " + withdrawalId + " does not exist."));

        withdrawal.triggerProcessing(withdrawalProcessingService, userRepository);

        this.withdrawalRepository.save(withdrawal);

        return new WithdrawalRepresentation(withdrawal);
    }

    @Transactional
    public WithdrawalRepresentation handleWithdrawalProcessingCompletion(long withdrawalId, boolean success) {
        var withdrawal = this.withdrawalRepository.getWithdrawalOfId(withdrawalId).orElseThrow(
                () -> new IllegalArgumentException("Withdrawal of ID: " + withdrawalId + " does not exist."));

        if (success) {
            withdrawal.handleProcessingSuccess();
        } else {
            withdrawal.handleProcessingFailure();
        }

        this.withdrawalRepository.save(withdrawal);

        return new WithdrawalRepresentation(withdrawal);
    }

    @Transactional(readOnly = true)
    public List<WithdrawalRepresentation> getAllWithdrawals() {
        return this.withdrawalRepository.getAll()
                .stream().map(WithdrawalRepresentation::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<WithdrawalRepresentation> getWithdrawal(long withdrawalId) {
        return this.withdrawalRepository.getWithdrawalOfId(withdrawalId).map(WithdrawalRepresentation::new);
    }
}
