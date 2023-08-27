package com.wezaam.withdrawal.controller;

import com.wezaam.withdrawal.model.InstantWithdrawal;
import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import com.wezaam.withdrawal.model.WithdrawalStatus;
import com.wezaam.withdrawal.model.dto.WithdrawalScheduledDto;
import com.wezaam.withdrawal.service.PaymentMethodService;
import com.wezaam.withdrawal.service.UserService;
import com.wezaam.withdrawal.service.WithdrawalService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
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
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/create-withdrawals")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@Valid @RequestBody WithdrawalScheduledDto withdrawalScheduledDto) throws ParseException {
        Withdrawal withdrawal = convertToEntity(withdrawalScheduledDto);
        if(!withdrawalService.isValidWithdrawalUser(withdrawal.getUserId())){
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        if(!withdrawalService.isValidWithdrawalPaymentMethod(withdrawal.getPaymentMethodId())) {
            return new ResponseEntity("Payment method not found", HttpStatus.NOT_FOUND);
        }
        withdrawalService.handleWithdrawal(withdrawal);

        return new ResponseEntity(withdrawal, HttpStatus.OK);
    }

    @GetMapping("/find-all-withdrawals")
    public ResponseEntity findAll() {
        List<InstantWithdrawal> instantWithdrawals = withdrawalService.findAllWithdrawals();
        List<WithdrawalScheduled> withdrawalsScheduled = withdrawalService.findAllWithdrawalsScheduled();
        List<Object> result = new ArrayList<>();
        result.addAll(instantWithdrawals);
        result.addAll(withdrawalsScheduled);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    private Withdrawal convertToEntity(WithdrawalScheduledDto withdrawalScheduledDto) throws ParseException {
        if(withdrawalScheduledDto.getExecuteAt().equals("ASAP")) {
            InstantWithdrawal instantWithdrawal = modelMapper.map(withdrawalScheduledDto, InstantWithdrawal.class);
            instantWithdrawal.setCreatedAt(Instant.now());
            instantWithdrawal.setStatus(WithdrawalStatus.PENDING);
            return instantWithdrawal;
        } else {
            WithdrawalScheduled withdrawalScheduled = modelMapper.map(withdrawalScheduledDto, WithdrawalScheduled.class);
            withdrawalScheduled.setCreatedAt(Instant.now());
            withdrawalScheduled.setStatus(WithdrawalStatus.PENDING);
            return withdrawalScheduled;
        }
    }
}
