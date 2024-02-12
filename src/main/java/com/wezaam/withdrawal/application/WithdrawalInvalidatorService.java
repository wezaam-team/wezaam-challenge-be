package com.wezaam.withdrawal.application;

import com.wezaam.withdrawal.application.event.EventPublisher;
import com.wezaam.withdrawal.domain.Withdrawal;
import com.wezaam.withdrawal.domain.WithdrawalRepository;
import com.wezaam.withdrawal.domain.WithdrawalStatus;
import com.wezaam.withdrawal.domain.event.WithdrawalInvalidatedConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.logging.Logger;

@Service
public class WithdrawalInvalidatorService {

    private static final Logger LOGGER =
            Logger.getLogger(WithdrawalInvalidatorService.class.getSimpleName());

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Transactional(Transactional.TxType.REQUIRED)
    public void invalidateWithdrawalWithStatus(final Withdrawal withdrawal, WithdrawalStatus status) {
        withdrawal.setWithdrawalStatus(status);
        withdrawalRepository.save(withdrawal);

        try {
            eventPublisher.publish(
                    WithdrawalInvalidatedConverter
                            .aWithdrawalInvalidatedConverter()
                            .apply(withdrawal)
            );
        } catch (Exception e) {
            LOGGER.severe(
                    String.format(
                            "Impossible to produce the withdrawal invalidation event for withdrawal #%d. Reason: %s",
                            withdrawal.getId(),
                            e.getMessage()
                    )
            );

        }
    }
}
