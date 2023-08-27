package com.wezaam.withdrawal.controller;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import com.wezaam.withdrawal.model.WithdrawalStatus;
import com.wezaam.withdrawal.service.PaymentMethodService;
import com.wezaam.withdrawal.service.UserService;
import com.wezaam.withdrawal.service.WithdrawalService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class WithdrawalController {

    @Autowired
    private UserService userService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private WithdrawalService withdrawalService;

    @PostMapping("/create-withdrawals")
    public ResponseEntity create(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String paymentMethodId = request.getParameter("paymentMethodId");
        String amount = request.getParameter("amount");
        String executeAt = request.getParameter("executeAt");
        if (userId == null || paymentMethodId == null || amount == null || executeAt == null) {
            return new ResponseEntity("Required params are missing", HttpStatus.BAD_REQUEST);
        }
        try {
            userService.findById(Long.parseLong(userId));
        } catch (Exception e) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        try {
            paymentMethodService.findById(Long.parseLong(paymentMethodId));
        } catch (Exception e) {
            return new ResponseEntity("Payment method not found", HttpStatus.NOT_FOUND);
        }

        Object body;
        if (executeAt.equals("ASAP")) {
            Withdrawal withdrawal = new Withdrawal();
            withdrawal.setUserId(Long.parseLong(userId));
            withdrawal.setPaymentMethodId(Long.parseLong(paymentMethodId));
            withdrawal.setAmount(Double.parseDouble(amount));
            withdrawal.setCreatedAt(Instant.now());
            withdrawal.setStatus(WithdrawalStatus.PENDING);
            withdrawalService.create(withdrawal);
            body = withdrawal;
        } else {
            WithdrawalScheduled withdrawalScheduled = new WithdrawalScheduled();
            withdrawalScheduled.setUserId(Long.parseLong(userId));
            withdrawalScheduled.setPaymentMethodId(Long.parseLong(paymentMethodId));
            withdrawalScheduled.setAmount(Double.parseDouble(amount));
            withdrawalScheduled.setCreatedAt(Instant.now());
            withdrawalScheduled.setExecuteAt(Instant.parse(executeAt));
            withdrawalScheduled.setStatus(WithdrawalStatus.PENDING);
            withdrawalService.schedule(withdrawalScheduled);
            body = withdrawalScheduled;
        }

        return new ResponseEntity(body, HttpStatus.OK);
    }

    @GetMapping("/find-all-withdrawals")
    public ResponseEntity findAll() {
        List<Withdrawal> withdrawals = withdrawalService.findAllWithdrawals();
        List<WithdrawalScheduled> withdrawalsScheduled = withdrawalService.findAllWithdrawalsScheduled();
        List<Object> result = new ArrayList<>();
        result.addAll(withdrawals);
        result.addAll(withdrawalsScheduled);

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
