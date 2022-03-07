package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.domain.entities.Employee;
import com.wezaam.withdrawal.domain.entities.Withdraw;
import com.wezaam.withdrawal.domain.entities.WithdrawStatusEnum;
import com.wezaam.withdrawal.domain.repositories.EmployeeRepository;
import com.wezaam.withdrawal.domain.repositories.WithdrawRepository;
import com.wezaam.withdrawal.exception.BusinessException;
import com.wezaam.withdrawal.exception.NotificationException;
import com.wezaam.withdrawal.gateway.ProviderClient;
import com.wezaam.withdrawal.mapper.WithdrawMapper;
import com.wezaam.withdrawal.rest.dto.RestApiResponse;
import com.wezaam.withdrawal.rest.dto.WithdrawalRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;


@Service
@Slf4j
public class WithdrawalService {


    @Autowired
    private WithdrawMapper mapperService;

    @Autowired
    private ProviderClient providerClient;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmployeeRepository employerRepository;

    @Autowired
    private WithdrawRepository withdrawRepository;

    public RestApiResponse createWithdrawalProcess(WithdrawalRequest withdrawalRequest) throws Exception {
        final Employee EmployeeEntity = retrieveEmployee(withdrawalRequest.getEmployeeId());
        final Withdraw withDrawEntity = mapperService.map(withdrawalRequest, EmployeeEntity);

        validateWithdrawOperation(withDrawEntity);

        createWithdrawTransaction(withDrawEntity);

        executeWithdraw(withDrawEntity);

        return buildResponse(withDrawEntity);
    }

    public void validateWithdrawOperation(Withdraw withdraw) {
        // validate amount
        Optional.of(withdraw.getAmount())
                .filter(amount -> withdraw.getEmployee().getTotalBalance().compareTo(amount) >= 0)
                .orElseThrow(() -> new BusinessException(BusinessException.ERROR_TYPE.NOT_ENOUGH_BALANCE));
        // validate paymentMethod
        Optional.of(withdraw.getPaymentMethod())
                .filter(paymentMethod -> withdraw.getEmployee().getPaymentMethods().stream()
                        .anyMatch(EmployeePaymentMethod -> paymentMethod.equals(EmployeePaymentMethod.getAlias())))
                .orElseThrow(() -> new BusinessException(BusinessException.ERROR_TYPE.PAYMENT_METHOD_NOT_FOUND));
        // validate other withdraw in execution
        if (withdrawRepository.existsByEmployeeAndStatus(withdraw.getEmployee(), WithdrawStatusEnum.CREATED)) {
            throw new BusinessException(BusinessException.ERROR_TYPE.ALREADY_OPERATION_RUNNING);
        }
    }

    public void executeTransaction(Withdraw withdrawDataResult) {
        providerClient.callProvider(withdrawDataResult)
                .doOnSuccess(providerResponse -> {
                    successTransaction(withdrawDataResult);
                })
                .doOnError(throwable -> errorTransaction(withdrawDataResult))
                .subscribe();

    }

    private void executeWithdraw(Withdraw withdrawDataResult) {
        if (withdrawDataResult.getExecutionDate().compareTo(LocalDate.now()) <= 0) {
            executeTransaction(withdrawDataResult);
        } else {
            updateWithdraw(withdrawDataResult, WithdrawStatusEnum.SCHEDULED);
        }
    }


    private Employee retrieveEmployee(String EmployeeId) {
        return employerRepository.findById(EmployeeId)
                .orElseThrow(() -> new BusinessException(BusinessException.ERROR_TYPE.EMPLOYEE_NOT_FOUND));
    }


    private Withdraw createWithdrawTransaction(Withdraw withdraw) throws Exception {
        withdraw.setStatus(WithdrawStatusEnum.CREATED);
        return withdrawRepository.save(withdraw);

    }

    private void successTransaction(Withdraw withdrawDataResult) {
        updateWithdrawStatusAndNotify(withdrawDataResult, WithdrawStatusEnum.PROCESSED,
                (status) -> {
                    updateWithdraw(withdrawDataResult, status);
                });
        updateBalanceEmployee(withdrawDataResult);
    }

    private void updateBalanceEmployee(Withdraw withdrawDataResult) {
        final Employee employee = withdrawDataResult.getEmployee();
        final BigDecimal newBalance = employee.getTotalBalance().subtract(withdrawDataResult.getAmount());
        employee.setTotalBalance(newBalance);
        employerRepository.save(employee);
    }

    private void errorTransaction(Withdraw withdrawDataResult) {
        updateWithdrawStatusAndNotify(withdrawDataResult, WithdrawStatusEnum.FAILED,
                null);
    }


    private void updateWithdrawStatusAndNotify(Withdraw withdraw,
                                               WithdrawStatusEnum status,
                                               Consumer<WithdrawStatusEnum> notifiedConsumer) {
        updateWithdraw(withdraw, status);
        try {
            notificationService.emailSender(withdraw.getEmployee(), status);
            if (Objects.nonNull(notifiedConsumer)) {
                notifiedConsumer.accept(WithdrawStatusEnum.NOTIFIED);
            }
        } catch (NotificationException ex) {
            log.error("withdrawId {} , message {}", withdraw.getId(), ex.getMessage());
        }
    }

    private RestApiResponse buildResponse(Withdraw withdrawDataResult) {
        return RestApiResponse.builder().id(String.valueOf(withdrawDataResult.getId())).build();
    }


    public void updateWithdraw(Withdraw withdrawDataResult, WithdrawStatusEnum status) {
        withdrawDataResult.setStatus(status);
        withdrawRepository.save(withdrawDataResult);

    }

}

