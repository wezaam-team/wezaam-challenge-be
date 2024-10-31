package com.wezaam.withdrawal.application.impl.processor;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.exception.NotFoundStrategyProcessorException;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WithdrawalTypeStrategy {

    private final List<WithdrawalTypeProcessor> withdrawalTypeProcessors;

    public WithdrawalTypeProcessor getWithdrawalProcessor(WithdrawalType withdrawalType) {
        return withdrawalTypeProcessors.stream()
                .filter(bean -> bean.getType().equals(withdrawalType))
                .findFirst()
                .orElseThrow(
                        () ->
                                new NotFoundStrategyProcessorException(
                                        "There is not implementation for that processor: "
                                                + withdrawalType));
    }
}
