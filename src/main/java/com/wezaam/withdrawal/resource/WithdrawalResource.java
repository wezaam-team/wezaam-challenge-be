package com.wezaam.withdrawal.resource;

import com.wezaam.withdrawal.application.WithdrawalApplicationService;
import com.wezaam.withdrawal.application.representation.WithdrawalRepresentation;
import io.swagger.annotations.Api;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Api
@RestController
public class WithdrawalResource {

    private final WithdrawalApplicationService withdrawalApplicationService;

    public WithdrawalResource(WithdrawalApplicationService withdrawalApplicationService) {
        if (withdrawalApplicationService == null) {
            throw new IllegalArgumentException("The withdrawalApplicationService should not be null.");
        }

        this.withdrawalApplicationService = withdrawalApplicationService;
    }

    @PostMapping("/withdrawals")
    @ResponseStatus(HttpStatus.CREATED)
    public WithdrawalRepresentation createWithdrawal(@RequestBody CreateWithdrawalRequest request) {
        var userId = request.getUserId();
        var paymentMethodId = request.getPaymentMethodId();
        var amount = request.getAmount();
        var executeAtString = request.getExecuteAt();

        var requiredPropertiesMissing = new ArrayList<String>();

        if (userId == null) {
            requiredPropertiesMissing.add("userId");
        }

        if (paymentMethodId == null) {
            requiredPropertiesMissing.add("paymentMethodId");
        }

        if (amount == null) {
            requiredPropertiesMissing.add("amount");
        }

        if (executeAtString == null) {
            requiredPropertiesMissing.add("executeAtString");
        }

        if (!requiredPropertiesMissing.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The following required properties are missing: "
                    + String.join(", ", requiredPropertiesMissing) + ".");
        }

        var executeAt = "ASAP".equals(executeAtString) ? null : Instant.parse(executeAtString);

        return this.withdrawalApplicationService.createWithdrawal(userId, paymentMethodId, amount, executeAt);
    }

    @GetMapping("/withdrawals")
    public List<WithdrawalRepresentation> getAllWithdrawals() {
        return this.withdrawalApplicationService.getAllWithdrawals();
    }

    @GetMapping("/withdrawals/{id}")
    public WithdrawalRepresentation getWithdrawal(@PathVariable Long id) {
        return this.withdrawalApplicationService.getWithdrawal(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Withdrawal of ID: " + id + " does not exist."));
    }
}
