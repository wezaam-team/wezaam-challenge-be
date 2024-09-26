package com.wezaam.withdrawal.rest;

import com.wezaam.withdrawal.domain.WithdrawalListResponse;
import com.wezaam.withdrawal.domain.WithdrawalRequest;
import com.wezaam.withdrawal.model.*;
import com.wezaam.withdrawal.service.PaymentService;
import com.wezaam.withdrawal.service.UserService;
import com.wezaam.withdrawal.service.WithdrawalService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Api
@RestController
public class WithdrawalController {

    //@Autowired
    //private ApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WithdrawalService withdrawalService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/create-withdrawal")
    public ResponseEntity<Withdrawal> createWithdrawal(@RequestBody WithdrawalRequest request) {

        User user = null;
        PaymentMethod paymentMethod = null;
        try {
           user = userService.findByName(request.getName());
           paymentMethod = paymentService.findByName(request.getPaymentMethod());

            Withdrawal withdrawal = new Withdrawal();
            withdrawal.setUserId(user.getId());
            withdrawal.setPaymentMethodId(paymentMethod.getId());
            withdrawal.setAmount(request.getAmount());
            withdrawal.setCreatedAt(Instant.now());
            withdrawal.setStatus(WithdrawalStatus.PENDING);
            withdrawalService.create(withdrawal);

            return new ResponseEntity(withdrawal, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(request, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create-withdrawal-scheduled")
    public ResponseEntity<WithdrawalScheduled> createWithdrawalScheduled(@RequestBody WithdrawalRequest request) throws Exception {

        User user = null;
        PaymentMethod paymentMethod = null;

        try {

            user = userService.findByName(request.getName());
            paymentMethod = paymentService.findByName(request.getPaymentMethod());

            WithdrawalScheduled withdrawalScheduled = new WithdrawalScheduled();
            withdrawalScheduled.setUserId(user.getId());
            withdrawalScheduled.setPaymentMethodId(paymentMethod.getId());
            withdrawalScheduled.setAmount(request.getAmount());
            withdrawalScheduled.setCreatedAt(Instant.now());
            withdrawalScheduled.setExecuteAt(LocalDateTime.parse(request.getExecutedAt(), formatter).atZone(ZoneId.systemDefault()).toInstant());
            withdrawalScheduled.setStatus(WithdrawalStatus.PENDING);
            withdrawalService.schedule(withdrawalScheduled);

            return new ResponseEntity(withdrawalScheduled, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(request, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/find-all-withdrawals")
    public ResponseEntity<WithdrawalListResponse> findAll() {
        return new ResponseEntity(withdrawalService.findAll(), HttpStatus.OK);
    }
}
