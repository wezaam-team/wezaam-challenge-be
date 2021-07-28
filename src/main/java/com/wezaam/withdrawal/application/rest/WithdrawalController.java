package com.wezaam.withdrawal.application.rest;

import com.wezaam.withdrawal.application.command.CreateWithdrawalCommand;
import com.wezaam.withdrawal.application.command.CreateWithdrawalCommandBuilder;
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
            final CreateWithdrawalCommand createWithdrawalCommand =
                    CreateWithdrawalCommandBuilder.aCreateWithdrawalCommandBuilder()
                            .withPaymentMethodId(createWithdrawalRequest.getPaymentMethodId())
                            .withUserId(createWithdrawalRequest.getUserId())
                            .withAmount(createWithdrawalRequest.getAmount())
                            .withImmediate(createWithdrawalRequest.getImmediate())
                            .withScheduledFor(createWithdrawalRequest.getScheduledFor())
                            .build();

            final Withdrawal withdrawal = withdrawalService.createWithdrawal(createWithdrawalCommand);

            final CreateWithdrawalResponse createWithdrawalResponse = new CreateWithdrawalResponse();
            createWithdrawalResponse.setId(withdrawal.getId());
            createWithdrawalResponse.setPaymentMethodId(withdrawal.getUser().getId());
            createWithdrawalResponse.setUserId(withdrawal.getPaymentMethod().getId());
            createWithdrawalResponse.setAmount(withdrawal.getAmount());
            createWithdrawalResponse.setImmediate(withdrawal.getImmediate());
            createWithdrawalResponse.setScheduledFor(withdrawal.getScheduledFor());
            createWithdrawalResponse.setWithdrawalStatus(withdrawal.getWithdrawalStatus());

            return ResponseEntity.created(
                    URI.create(
                            String.format("/withdrawals/%d", withdrawal.getId())))
                    .body(createWithdrawalResponse);

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
        ResponseEntity<GetWithdrawalResponse> withdrawalResponse;

        if (withdrawal.isPresent()) {
            final GetWithdrawalResponse getWithdrawalResponse = new GetWithdrawalResponse();
            getWithdrawalResponse.setId(withdrawal.get().getId());
            getWithdrawalResponse.setPaymentMethodId(withdrawal.get().getUser().getId());
            getWithdrawalResponse.setUserId(withdrawal.get().getPaymentMethod().getId());
            getWithdrawalResponse.setAmount(withdrawal.get().getAmount());
            getWithdrawalResponse.setImmediate(withdrawal.get().getImmediate());
            getWithdrawalResponse.setScheduledFor(withdrawal.get().getScheduledFor());
            getWithdrawalResponse.setWithdrawalStatus(withdrawal.get().getWithdrawalStatus());
            withdrawalResponse = ResponseEntity.ok(getWithdrawalResponse);
        } else {
            withdrawalResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return withdrawalResponse;
    }
}
