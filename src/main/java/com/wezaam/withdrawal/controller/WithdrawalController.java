package com.wezaam.withdrawal.controller;

import com.wezaam.withdrawal.model.WithdrawalInstant;
import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import com.wezaam.withdrawal.model.WithdrawalStatus;
import com.wezaam.withdrawal.model.dto.WithdrawalScheduledDto;
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
    private WithdrawalService withdrawalService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/create-withdrawals")
    public ResponseEntity create(@Valid @RequestBody WithdrawalScheduledDto withdrawalScheduledDto) throws ParseException {
        Withdrawal withdrawal = convertToEntity(withdrawalScheduledDto);
        try {
            withdrawalService.handleWithdrawal(withdrawal);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        //not converting back to a DTO - do not see any sensitive data in it and don't know the full requirements
        return new ResponseEntity(withdrawal, HttpStatus.OK);
    }

    @GetMapping("/find-all-withdrawals")
    public ResponseEntity findAll() {
        List<WithdrawalInstant> withdrawalInstants = withdrawalService.findAllWithdrawals();
        List<WithdrawalScheduled> withdrawalsScheduled = withdrawalService.findAllWithdrawalsScheduled();
        List<Object> result = new ArrayList<>();
        result.addAll(withdrawalInstants);
        result.addAll(withdrawalsScheduled);
        //not converting back to a DTO - do not see any sensitive data in it and don't know the full requirements
        return new ResponseEntity(result, HttpStatus.OK);
    }

    private Withdrawal convertToEntity(WithdrawalScheduledDto withdrawalScheduledDto) throws ParseException {
        if(withdrawalScheduledDto.getExecuteAt().equals("ASAP")) {
            WithdrawalInstant withdrawalInstant = modelMapper.map(withdrawalScheduledDto, WithdrawalInstant.class);
            withdrawalInstant.setCreatedAt(Instant.now());
            withdrawalInstant.setStatus(WithdrawalStatus.PENDING);
            return withdrawalInstant;
        } else {
            WithdrawalScheduled withdrawalScheduled = modelMapper.map(withdrawalScheduledDto, WithdrawalScheduled.class);
            withdrawalScheduled.setCreatedAt(Instant.now());
            withdrawalScheduled.setStatus(WithdrawalStatus.PENDING);
            return withdrawalScheduled;
        }
    }
}
