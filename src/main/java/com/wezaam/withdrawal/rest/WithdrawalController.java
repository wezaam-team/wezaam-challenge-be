package com.wezaam.withdrawal.rest;

import com.wezaam.withdrawal.model.dto.WithdrawalDto;
import com.wezaam.withdrawal.service.WithdrawalService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
@RequiredArgsConstructor
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    @PostMapping("/create-withdrawals")
    @ResponseStatus(HttpStatus.OK)
    public WithdrawalDto create(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "paymentMethodId") Long paymentMethodId,
            @RequestParam(value = "amount") Double amount,
            @RequestParam(value = "executeAt") String executeAt) {
        return WithdrawalDto.build(withdrawalService.create(userId, paymentMethodId, amount, executeAt));
    }

    @GetMapping("/find-all-withdrawals")
    @ResponseStatus(HttpStatus.OK)
    public List<WithdrawalDto> findAll() {
        return withdrawalService.findAll().stream()
                .map(WithdrawalDto::build)
                .collect(Collectors.toList());
    }
}
