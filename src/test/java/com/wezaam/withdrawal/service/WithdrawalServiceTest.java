package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalStatus;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.repository.WithdrawalRepository;
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.service.event.WithdrawalEventPublisher;
import com.wezaam.withdrawal.service.impl.WithdrawalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WithdrawalServiceTest {

    @InjectMocks
    WithdrawalServiceImpl withdrawalService;
    @Mock
    WithdrawalRepository withdrawalRepository;
    @Mock
    WithdrawalScheduledRepository withdrawalScheduledRepository;
    @Mock
    WithdrawalProcessingService withdrawalProcessingService;
    @Mock
    PaymentMethodRepository paymentMethodRepository;
    @Mock
    EventsService eventsService;
    @Mock
    WithdrawalEventPublisher withdrawalEventPublisher;

    @Test
    void shouldCreateAndPublishWithdrawal(){
        Withdrawal withdrawal = new Withdrawal();

        withdrawalService.create(withdrawal);

        verify(withdrawalRepository, times(1)).save(any());
        verify(withdrawalEventPublisher, times(1)).publishWithdrawalEvent(any());
    }

    @Test
    void shouldProcessWithdrawal() throws TransactionException {
        Withdrawal withdrawal = createWithdrawal();
        PaymentMethod paymentMethod = new PaymentMethod();
        when(withdrawalRepository.findById(1L)).thenReturn(Optional.of(withdrawal));
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(paymentMethod));
        when(withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethod)).thenReturn(123L);

        withdrawalService.processWithdrawal(withdrawal);

        verify(withdrawalRepository).save(any());
        verify(eventsService).send(any(Withdrawal.class));
        assertThat(withdrawal)
                .extracting(Withdrawal::getTransactionId, Withdrawal::getStatus)
                .containsExactlyInAnyOrder(123L, WithdrawalStatus.PROCESSING);

    }

    @Test
    void shouldSetFailedStatusOnTransactionException() throws TransactionException {
        Withdrawal withdrawal = createWithdrawal();
        PaymentMethod paymentMethod = new PaymentMethod();
        when(withdrawalRepository.findById(1L)).thenReturn(Optional.of(withdrawal));
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(paymentMethod));
        when(withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethod)).thenThrow(TransactionException.class);

        withdrawalService.processWithdrawal(withdrawal);

        verify(withdrawalRepository).save(any());
        verify(eventsService).send(any(Withdrawal.class));
        assertThat(withdrawal)
                .extracting(Withdrawal::getTransactionId, Withdrawal::getStatus)
                .containsExactlyInAnyOrder(null, WithdrawalStatus.FAILED);

    }

    @Test
    void shouldSetInternalErrorStatusOnException() throws TransactionException {
        Withdrawal withdrawal = createWithdrawal();
        PaymentMethod paymentMethod = new PaymentMethod();
        when(withdrawalRepository.findById(1L)).thenReturn(Optional.of(withdrawal));
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(paymentMethod));
        when(withdrawalProcessingService.sendToProcessing(withdrawal.getAmount(), paymentMethod)).thenThrow(RuntimeException.class);

        withdrawalService.processWithdrawal(withdrawal);

        verify(withdrawalRepository).save(any());
        verify(eventsService).send(any(Withdrawal.class));
        assertThat(withdrawal)
                .extracting(Withdrawal::getTransactionId, Withdrawal::getStatus)
                .containsExactlyInAnyOrder(null, WithdrawalStatus.INTERNAL_ERROR);

    }

    private Withdrawal createWithdrawal(){
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(1L);
        withdrawal.setPaymentMethodId(1L);
        withdrawal.setAmount(10d);

        return withdrawal;
    }


}
