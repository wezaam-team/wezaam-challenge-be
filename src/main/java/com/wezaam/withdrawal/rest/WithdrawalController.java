package com.wezaam.withdrawal.rest;

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO;
import com.wezaam.withdrawal.model.WithdrawType;
import com.wezaam.withdrawal.service.WithdrawalService;
import io.swagger.annotations.Api;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api
@RestController
@RequestMapping("withdrawals")
public class WithdrawalController {

    @Autowired
    private WithdrawalService service;

    @PostMapping(path = "/")
    public WithdrawalInformationDTO create(@Valid @RequestBody WithdrawalInformationDTO withdrawalRequest){
        val persistedWithdrawal = service.create(withdrawalRequest);
        if (persistedWithdrawal.getType() == WithdrawType.ASAP) {
            return service.process(persistedWithdrawal);
        } else {
            return persistedWithdrawal;
        }
    }

    @GetMapping("/")
    public List<WithdrawalInformationDTO> findAll() {
        return service.findAllWithdrawals();
    }
}
