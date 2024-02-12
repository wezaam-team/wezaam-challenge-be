package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.ForbiddenException;
import com.wezaam.withdrawal.exception.NotFoundException;
import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.enums.WithdrawalStatus;
import com.wezaam.withdrawal.repository.WithdrawalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;
    private final WithdrawalProcessingService withdrawalProcessingService;
    private final PaymentMethodService paymentMethodService;
    private final EventsService eventsService;
    private final UserService userService;
    private final ExecutorService executorService;

    public Withdrawal create(Long userId, Long paymentMethodId, Double amount, String executeAt) {
        User user = userService.findById(userId);
        if (amount > user.getMaxWithdrawalAmount()) {
            throw new ForbiddenException("Max withdrawal amount of " + user.getMaxWithdrawalAmount() + " exceeded");
        }
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodId);
        if (user.getPaymentMethods().stream()
                .map(PaymentMethod::getId)
                .noneMatch(id -> id.equals(paymentMethod.getId()))) {
            throw new ForbiddenException("User does not have payment method " + paymentMethod.getName());
        }

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setUserId(userId);
        withdrawal.setPaymentMethodId(paymentMethodId);
        withdrawal.setAmount(amount);
        withdrawal.setCreatedAt(Instant.now());
        if (executeAt.equals("ASAP")) {
            withdrawal.setExecuteAt(Instant.now());
            processAsync(withdrawal);
        } else {
            withdrawal.setStatus(WithdrawalStatus.PENDING);
            withdrawal.setExecuteAt(Instant.parse(executeAt));
            withdrawalRepository.save(withdrawal);
        }
        return findById(withdrawal.getId());
    }

    @Scheduled(fixedDelay = 5000)
    public void run() {
        withdrawalRepository.findAllByExecuteAtBeforeAndStatus(Instant.now(), WithdrawalStatus.PENDING)
                .forEach(this::processAsync);
    }

    public void processAsync(Withdrawal withdrawal) {
        executorService.submit(() -> process(withdrawal));
    }

    protected void process(Withdrawal withdrawal) {
        try {
            withdrawal.setStatus(WithdrawalStatus.PROCESSING);
            withdrawalRepository.save(withdrawal);
            PaymentMethod paymentMethod = paymentMethodService.findById(withdrawal.getPaymentMethodId());
            withdrawal.setTransactionId(withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethod));
        } catch (TransactionException transactionException) {
            withdrawal.setStatus(WithdrawalStatus.FAILED);
        } catch (Exception exception) {
            withdrawal.setStatus(WithdrawalStatus.INTERNAL_ERROR);
        } finally {
            withdrawalRepository.save(withdrawal);
            eventsService.send(withdrawal);
        }
    }

    public List<Withdrawal> findAll() {
        return withdrawalRepository.findAll();
    }

    public Withdrawal findById(Long withdrawalId) {
        return withdrawalRepository.findById(withdrawalId)
                .orElseThrow(() -> new NotFoundException("Withdrawal not found for the id:" + withdrawalId));
    }
}
