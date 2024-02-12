package com.wezaam.withdrawal.infrastructure;

import com.wezaam.withdrawal.domain.Provider;
import com.wezaam.withdrawal.domain.Withdrawal;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.logging.Logger;

@Service
public class RestClientProvider implements Provider {

    private static final Logger LOGGER =
            Logger.getLogger(RestClientProvider.class.getSimpleName());

    @Override
    @Async
    public void processWithdrawal(Withdrawal withdrawal) {
        LOGGER.info(
                String.format(
                        "Sending withdrawal #%d with amount %s from user #%d to payment provider #%d",
                        withdrawal.getId(),
                        NumberFormat.getCurrencyInstance().format(withdrawal.getAmount()),
                        withdrawal.getUser().getId(),
                        withdrawal.getPaymentMethod().getId())
        );
    }
}
