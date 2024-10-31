package com.wezaam.withdrawal.infrastructure.rest.adapter;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wezaam.withdrawal.application.WithdrawalService;
import com.wezaam.withdrawal.domain.command.CreateWithdrawalCommand;
import com.wezaam.withdrawal.domain.entity.Withdrawal;
import com.wezaam.withdrawal.infrastructure.rest.dto.create.CreateWithdrawalRequest;
import com.wezaam.withdrawal.infrastructure.rest.dto.response.WithdrawalResponse;
import com.wezaam.withdrawal.infrastructure.rest.mapper.WithdrawalMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WithdrawalController {

    private final WithdrawalService withdrawalService;
    private final WithdrawalMapper withdrawalMapper;

    @PostMapping(
            value = "/withdrawals",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WithdrawalResponse> create(
            @Valid @RequestBody CreateWithdrawalRequest withdrawalRequest) {

        log.info("Request withdrawal {}", withdrawalRequest);
        CreateWithdrawalCommand command =
                withdrawalMapper.mapAsCreateWithdrawalCommand(withdrawalRequest);
        Withdrawal withdrawal = withdrawalService.create(command);
        WithdrawalResponse withdrawalResponse =
                withdrawalMapper.mapWithdrawalAsWithdrawalResponse(withdrawal);
        return ResponseEntity.status(HttpStatus.CREATED).body(withdrawalResponse);
    }

    @GetMapping(value = "/withdrawals", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WithdrawalResponse>> findAll() {
        log.info("Request all withdrawal");
        List<Withdrawal> result = withdrawalService.getAllWithdrawal();
        return ResponseEntity.ok(
                withdrawalMapper.mapWithdrawalListAsWithdrawalResponseList(result));
    }
}
