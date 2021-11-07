package com.wezaam.withdrawal.rest;

import com.wezaam.withdrawal.exception.NoSuchPaymentMethodException;
import com.wezaam.withdrawal.exception.NoSuchUserException;
import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import com.wezaam.withdrawal.service.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@Api
@RestController
public class WithdrawalController {

    @Autowired
    private UserService userService;
    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private PaymentMethodService paymentMethodService;

    @PostMapping("/create-withdrawals")
    @ResponseStatus(HttpStatus.CREATED)
    public Withdrawal create(@Valid WithdrawalRequest request) {
        // TODO: Check requested user has payment method as well
        // FIXME: Delegate validation on service
        if (!userService.existsById(request.getUserId())) {
            throw new NoSuchUserException(request.getUserId());
        }
        if (!paymentMethodService.existsById(request.getPaymentMethodId())) {
            throw new NoSuchPaymentMethodException(request.getPaymentMethodId());
        }

        // TODO: Consider using a custom message deserializer or dto-entity converter
        Withdrawal result;
        if (request.getExecuteAt().equals("ASAP")) {
            Withdrawal withdrawal = new Withdrawal();
            withdrawal.setUserId(request.getUserId());
            withdrawal.setPaymentMethodId(request.getPaymentMethodId());
            withdrawal.setAmount(request.getAmount());
            result = withdrawalService.create(withdrawal);
        } else {
            WithdrawalScheduled withdrawalScheduled = new WithdrawalScheduled();
            withdrawalScheduled.setUserId(request.getUserId());
            withdrawalScheduled.setPaymentMethodId(request.getPaymentMethodId());
            withdrawalScheduled.setAmount(request.getAmount());
            withdrawalScheduled.setExecuteAt(Instant.parse(request.getExecuteAt()));
            result = withdrawalService.schedule(withdrawalScheduled);
        }

        return result;
    }

    @GetMapping("/find-all-withdrawals")
    @ResponseStatus(HttpStatus.OK)
    public List<Withdrawal> findAll() {
        return withdrawalService.findAll();
    }
}
