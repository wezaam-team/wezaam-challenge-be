package com.wezaam.withdrawal.infrastructure.external.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.infrastructure.external.payment.adapter.WithdrawalProcessingServiceImpl;

@ExtendWith(MockitoExtension.class)
class WithdrawalProcessingServiceImplTest {

    @InjectMocks WithdrawalProcessingServiceImpl underTest;

    @Test
    void shouldGet() {
        // arrange
        Double amount = 500D;
        PaymentMethod paymentMethod = PaymentMethod.builder().build();
        // act
        Long processingId = underTest.sendToProcessing(amount, paymentMethod);

        // assert
        assertThat(processingId).isNotNull();
    }
}
