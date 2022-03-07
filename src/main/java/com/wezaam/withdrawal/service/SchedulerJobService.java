package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.domain.entities.Employee;
import com.wezaam.withdrawal.domain.entities.Withdraw;
import com.wezaam.withdrawal.domain.entities.WithdrawStatusEnum;
import com.wezaam.withdrawal.domain.repositories.WithdrawRepository;
import com.wezaam.withdrawal.exception.BusinessException;
import com.wezaam.withdrawal.exception.NotificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class SchedulerJobService {

    private final static int SIZE = 10;

    @Autowired
    private WithdrawRepository withdrawRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WithdrawalService withdrawalService;

    // TODO This solution is not scalable, you can use schedlock  solutions or from an external job to avoid running multiple instances at the same time.
    @Scheduled(cron = "${scheduler.retry-notification-cron}")
    public void withdrawalRetryNotifications() {
        log.info("checking withdrawas to notify status ");

        notifyAllWithdrawProcessedByPage(PageRequest.of(0, SIZE));
    }

    // TODO This solution is not scalable, you can use schedlock solutions or from an external job to avoid running multiple instances at the same time.
    @Scheduled(cron = "${scheduler.execute-scheduled-cron}")
    public void withdrawalExecution() {
        log.info("scheduled withdraws");

        executeScheduledWithdraws(PageRequest.of(0, SIZE));
    }

    private void notifyAllWithdrawProcessedByPage(Pageable pageable) {

        final Page<Withdraw> pageContent = withdrawRepository.findByStatus(WithdrawStatusEnum.PROCESSED, pageable);
        pageContent.getContent().forEach(w -> notifyAndUpdateStatus(w));
        if (!pageContent.isLast()) {
            notifyAllWithdrawProcessedByPage(pageable);
        }
    }

    private void notifyAndUpdateStatus(Withdraw w) {
        try {
            final Employee employee = w.getEmployee();
            notificationService.emailSender(employee, w.getStatus());
            withdrawalService.updateWithdraw(w, WithdrawStatusEnum.NOTIFIED);
            log.info("withdraw {} updated to Notified", w.getId());
        } catch (NotificationException ex) {
            log.error("withdrawId {} , message {}", w.getId(), ex.getMessage());
        }
    }

    private void executeScheduledWithdraws(Pageable pageable) {
        Page<Withdraw> withdrawPage = withdrawRepository.findByStatusAndExecutionDate(WithdrawStatusEnum.SCHEDULED, LocalDate.now(), pageable);
        withdrawPage.getContent().forEach(w -> {
            try {
                withdrawalService.validateWithdrawOperation(w);
                withdrawalService.executeTransaction(w);
            } catch (BusinessException ex) {
                log.error("No se ha podido ejecutar la transacción programada {}, {} ", w.getId(), ex.getError().getDescription());
            }
        });
        if (!withdrawPage.isLast()) {
            executeScheduledWithdraws(pageable);
        }
    }
}
