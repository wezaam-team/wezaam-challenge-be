package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.NotFoundException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.wezaam.withdrawal.service.MocksHelper.mockPaymentMethod;
import static com.wezaam.withdrawal.service.MocksHelper.mockUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    @InjectMocks
    private PaymentMethodService paymentMethodService;

    @Test(expected = NotFoundException.class)
    public void testFindByIdWhenUserDoesNotExists() {
        Long paymentMethodId = 1l;
        when(paymentMethodRepository.findById(paymentMethodId)).thenThrow(new NotFoundException(""));

        paymentMethodService.findById(paymentMethodId);
    }

    @Test
    public void testFindByIdWhenUserExists() {
        Long paymentMethodId = 1l;
        PaymentMethod mockPaymentMethod = mockPaymentMethod(paymentMethodId);
        when(paymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.of(mockPaymentMethod));

        PaymentMethod result = paymentMethodService.findById(paymentMethodId);
        assertEquals(paymentMethodId, result.getId());
    }
}
