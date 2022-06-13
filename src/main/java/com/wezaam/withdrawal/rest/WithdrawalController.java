package com.wezaam.withdrawal.rest;

import com.wezaam.withdrawal.rest.adapter.WithdrawalAdapter;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.rest.api.withdrawal.CreateWithdrawalDto;
import com.wezaam.withdrawal.rest.api.withdrawal.WithdrawalDto;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Api
@RestController
public class WithdrawalController {

    private final UserController userController;
    private final PaymentMethodRepository paymentMethodRepository;
    private final WithdrawalAdapter withdrawalAdapter;

    public WithdrawalController(UserController userController,
                                PaymentMethodRepository paymentMethodRepository,
                                WithdrawalAdapter withdrawalAdapter) {
        this.userController = userController;
        this.paymentMethodRepository = paymentMethodRepository;
        this.withdrawalAdapter = withdrawalAdapter;
    }

    @PostMapping("/withdrawals")
    public ResponseEntity create(@Valid @RequestBody CreateWithdrawalDto withdrawalDto) {
        try {
            userController.findById(withdrawalDto.getUserId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (paymentMethodRepository.findById(withdrawalDto.getPaymentMethodId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment method not found");
        }

        withdrawalAdapter.create(withdrawalDto);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/withdrawals")
    public ResponseEntity<Collection<WithdrawalDto>> findAll() {
        return ResponseEntity.ok(withdrawalAdapter.findAll());
    }
}
