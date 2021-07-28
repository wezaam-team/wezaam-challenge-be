package com.wezaam.withdrawal.infrastructure;

import com.wezaam.withdrawal.domain.Provider;
import com.wezaam.withdrawal.domain.Withdrawal;
import org.springframework.stereotype.Service;

@Service
public class RestClientProvider implements Provider {

    @Override
    public void processWithdrawal(Withdrawal withdrawal) {

    }
}
