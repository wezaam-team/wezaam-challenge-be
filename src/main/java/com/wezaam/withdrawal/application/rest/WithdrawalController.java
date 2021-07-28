package com.wezaam.withdrawal.application.rest;

import com.wezaam.withdrawal.application.command.CreateWithdrawalCommandConverter;
import com.wezaam.withdrawal.domain.Withdrawal;
import com.wezaam.withdrawal.domain.WithdrawalService;
import com.wezaam.withdrawal.domain.exception.InsufficientAmountException;
import com.wezaam.withdrawal.domain.exception.InvalidPaymentMethodException;
import com.wezaam.withdrawal.domain.exception.InvalidScheduleException;
import com.wezaam.withdrawal.domain.exception.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/withdrawals")
public class WithdrawalController {

    @Autowired
    WithdrawalService withdrawalService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<CreateWithdrawalResponse> createWithdrawal(@NonNull @RequestBody CreateWithdrawalRequest createWithdrawalRequest) {
        try {
            final Withdrawal withdrawal = withdrawalService.createWithdrawal(
                    CreateWithdrawalCommandConverter
                            .aCreateWithdrawalCommandConverter()
                            .apply(createWithdrawalRequest)
            );

            return ResponseEntity.created(
                    URI.create(
                            String.format("/withdrawals/%d", withdrawal.getId())))
                    .body(
                            CreateWithdrawalResponseConverter
                                    .aCreateWithdrawalResponseConverter()
                                    .apply(withdrawal)
                    );

        } catch (UserDoesNotExistsException |
                InsufficientAmountException |
                InvalidPaymentMethodException |
                InvalidScheduleException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @GetMapping(
            value = "/{withdrawalId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<GetWithdrawalResponse> getWithdrawal(@PathVariable("withdrawalId") Long withdrawalId) {
        final Optional<Withdrawal> withdrawal = withdrawalService.getWithdrawal(withdrawalId);

        if (withdrawal.isPresent()) {
            return ResponseEntity.ok(
                    GetWithdrawalResponseConverter
                            .aGetWithdrawalResponseConverter()
                            .apply(withdrawal.get())
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
