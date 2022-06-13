package com.wezaam.withdrawal.adapter;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import com.wezaam.withdrawal.repository.WithdrawalRepository;
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository;
import com.wezaam.withdrawal.rest.adapter.WithdrawalAdapter;
import com.wezaam.withdrawal.rest.adapter.WithdrawalAdapterImpl;
import com.wezaam.withdrawal.rest.api.withdrawal.CreateWithdrawalDto;
import com.wezaam.withdrawal.rest.api.withdrawal.ScheduledWithdrawalDto;
import com.wezaam.withdrawal.rest.api.withdrawal.WithdrawalDto;
import com.wezaam.withdrawal.service.WithdrawalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawalAdapterTest {

    @InjectMocks
    private WithdrawalAdapterImpl withdrawalAdapter;

    @Mock
    private WithdrawalService withdrawalService;

    @Mock
    private WithdrawalRepository withdrawalRepository;

    @Mock
    private WithdrawalScheduledRepository withdrawalScheduledRepository;

    @Test
    void shouldCreateWithdrawalWhenExecutionTypeIsAsap(){
        CreateWithdrawalDto createWithdrawalDto = mock(CreateWithdrawalDto.class);
        when(createWithdrawalDto.getExecutionType()).thenReturn("ASAP");

        withdrawalAdapter.create(createWithdrawalDto);

        verify(withdrawalService).create(any());
    }

    @Test
    void shouldScheduleWithdrawalWhenExecutionTypeIsNotAsap(){
        CreateWithdrawalDto createWithdrawalDto = mock(CreateWithdrawalDto.class);

        withdrawalAdapter.create(createWithdrawalDto);

        verify(withdrawalService).schedule(any());
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoWithdrawals(){
        when(withdrawalRepository.findAll()).thenReturn(emptyList());
        when(withdrawalScheduledRepository.findAll()).thenReturn(emptyList());

        Collection<WithdrawalDto> withdrawals = withdrawalAdapter.findAll();

        assertThat(withdrawals)
                .isEmpty();
    }

    @Test
    void shouldReturnMixedWithdrawalsWhenThereAreWithdrawals(){
        Withdrawal withdrawal = mock(Withdrawal.class);
        WithdrawalScheduled withdrawalScheduled = mock(WithdrawalScheduled.class);
        when(withdrawalRepository.findAll()).thenReturn(List.of(withdrawal));
        when(withdrawalScheduledRepository.findAll()).thenReturn(List.of(withdrawalScheduled));

        Collection<WithdrawalDto> withdrawals = withdrawalAdapter.findAll();

        assertThat(withdrawals)
                .hasOnlyElementsOfTypes(ScheduledWithdrawalDto.class, WithdrawalDto.class)
                .hasSize(2);
    }
}
