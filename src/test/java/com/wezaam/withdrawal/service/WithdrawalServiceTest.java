package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.ForbiddenException;
import com.wezaam.withdrawal.exception.NotFoundException;
import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.enums.WithdrawalStatus;
import com.wezaam.withdrawal.repository.WithdrawalRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static com.wezaam.withdrawal.service.MocksHelper.mockPaymentMethod;
import static com.wezaam.withdrawal.service.MocksHelper.mockUser;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class WithdrawalServiceTest {

    @Mock
    private WithdrawalRepository withdrawalRepository;
    @Mock
    private WithdrawalProcessingService withdrawalProcessingService;
    @Mock
    private PaymentMethodService paymentMethodService;
    @Mock
    private EventsService eventsService;
    @Mock
    private UserService userService;
    @Mock
    private ExecutorService executorService;
    @InjectMocks
    private WithdrawalService withdrawalService;

    @Test(expected = NotFoundException.class)
    public void testCreateWithdrawalWithANoExistingUser() {
        Long userId = 1L;
        when(userService.findById(userId)).thenThrow(new NotFoundException(""));
        withdrawalService.create(userId, 2L, 100D, "ASAP");
    }

    @Test(expected = ForbiddenException.class)
    public void testCreateWithdrawalWithAnAmountHigherThanMaxAmount() {
        Long userId = 1L;
        Long paymentMethodId = 2L;
        when(userService.findById(userId)).thenReturn(mockUser(100d, paymentMethodId));
        withdrawalService.create(userId, paymentMethodId, 1000D, "ASAP");
    }

    @Test(expected = NotFoundException.class)
    public void testCreateWithdrawalWithANoExistingPaymentMethod() {
        Long userId = 1L;
        Long paymentMethodId = 2L;
        when(userService.findById(userId)).thenReturn(mockUser(100d, paymentMethodId));
        when(paymentMethodService.findById(paymentMethodId)).thenThrow(new NotFoundException(""));
        withdrawalService.create(userId, paymentMethodId, 100D, "ASAP");
    }

    @Test(expected = ForbiddenException.class)
    public void testCreateWithdrawalWithADifferentPaymentMethod() {
        Long userId = 1L;
        Long userPaymentMethodId = 2L;
        Long withdrawalPaymentMethodId = 3L;
        Double withdrawalAmount = 100D;
        Double maxWithdrawalAmount = 100D;

        when(userService.findById(userId)).thenReturn(mockUser(maxWithdrawalAmount, userPaymentMethodId));
        when(paymentMethodService.findById(withdrawalPaymentMethodId)).thenReturn(mockPaymentMethod(withdrawalPaymentMethodId));

        withdrawalService.create(userId, withdrawalPaymentMethodId, withdrawalAmount, "ASAP");
    }

    @Test
    public void testCreateWithdrawalStartingAsap() {
        Long userId = 1L;
        Long userPaymentMethodId = 2L;
        Long withdrawalPaymentMethodId = 2L;
        Double withdrawalAmount = 100D;
        Double maxWithdrawalAmount = 100D;
        Long withdrawalId = 1L;

        when(userService.findById(userId)).thenReturn(mockUser(maxWithdrawalAmount, userPaymentMethodId));
        when(paymentMethodService.findById(withdrawalPaymentMethodId)).thenReturn(mockPaymentMethod(withdrawalPaymentMethodId));
        when(withdrawalRepository.findById(any())).thenReturn(Optional.of(mockWithdrawal(withdrawalId, WithdrawalStatus.PROCESSING)));

        Withdrawal result = withdrawalService.create(userId, withdrawalPaymentMethodId, withdrawalAmount, "ASAP");

        assertEquals(WithdrawalStatus.PROCESSING, result.getStatus());
    }

    @Test
    public void testCreateWithdrawalStartingInTheFuture() {
        Long userId = 1L;
        Long userPaymentMethodId = 2L;
        Long withdrawalPaymentMethodId = 2L;
        Double withdrawalAmount = 100D;
        Double maxWithdrawalAmount = 100D;
        Long withdrawalId = 1L;

        when(userService.findById(userId)).thenReturn(mockUser(maxWithdrawalAmount, userPaymentMethodId));
        when(paymentMethodService.findById(withdrawalPaymentMethodId)).thenReturn(mockPaymentMethod(withdrawalPaymentMethodId));
        when(withdrawalRepository.findById(any())).thenReturn(Optional.of(mockWithdrawal(withdrawalId, WithdrawalStatus.PENDING)));

        Instant tomorrow = Instant.now().plus(1, ChronoUnit.DAYS);
        Withdrawal result = withdrawalService.create(userId, withdrawalPaymentMethodId, withdrawalAmount, tomorrow.toString());

        assertEquals(WithdrawalStatus.PENDING, result.getStatus());
    }

    private static Withdrawal mockWithdrawal(Long withdrawalId, WithdrawalStatus status) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(withdrawalId);
        withdrawal.setStatus(status);
        return withdrawal;
    }

    @Test
    public void runShouldCallExecutorServiceForEachWithdrawal() {
        Withdrawal mock1 = mockWithdrawal(1L, WithdrawalStatus.PENDING);
        Withdrawal mock2 = mockWithdrawal(2L, WithdrawalStatus.PENDING);
        Withdrawal mock3 = mockWithdrawal(3L, WithdrawalStatus.PENDING);
        when(withdrawalRepository.findAllByExecuteAtBeforeAndStatus(any(), eq(WithdrawalStatus.PENDING)))
                .thenReturn(asList(mock1, mock2, mock3));

        withdrawalService.run();

        verify(executorService, times(3)).submit(any(Runnable.class));
    }

    @Test
    public void testProcess() throws TransactionException {
        Long transactionId = 123L;
        Long withdrawalPaymentMethodId = 2L;
        Double withdrawalAmount = 100d;
        Withdrawal mockWithdrawal = mockWithdrawal(1L, WithdrawalStatus.PENDING);
        mockWithdrawal.setPaymentMethodId(withdrawalPaymentMethodId);
        mockWithdrawal.setAmount(withdrawalAmount);
        PaymentMethod mockPaymentMethod = mockPaymentMethod(withdrawalPaymentMethodId);
        when(paymentMethodService.findById(withdrawalPaymentMethodId)).thenReturn(mockPaymentMethod);
        when(withdrawalProcessingService.sendToProcessing(withdrawalAmount, mockPaymentMethod)).thenReturn(transactionId);

        withdrawalService.process(mockWithdrawal);

        Withdrawal expectedWithdrawalAfterTransaction = mockWithdrawal(1L, WithdrawalStatus.PROCESSING);
        expectedWithdrawalAfterTransaction.setPaymentMethodId(withdrawalPaymentMethodId);
        expectedWithdrawalAfterTransaction.setAmount(withdrawalAmount);
        expectedWithdrawalAfterTransaction.setTransactionId(transactionId);
        verify(withdrawalRepository, times(2)).save(expectedWithdrawalAfterTransaction);
    }

    @Test
    public void testProcessWithTransactionException() throws TransactionException {
        Long withdrawalPaymentMethodId = 2L;
        Double withdrawalAmount = 100d;
        Withdrawal mockWithdrawal = mockWithdrawal(1L, WithdrawalStatus.PENDING);
        mockWithdrawal.setPaymentMethodId(withdrawalPaymentMethodId);
        mockWithdrawal.setAmount(withdrawalAmount);
        PaymentMethod mockPaymentMethod = mockPaymentMethod(withdrawalPaymentMethodId);
        when(paymentMethodService.findById(withdrawalPaymentMethodId)).thenReturn(mockPaymentMethod);
        when(withdrawalProcessingService.sendToProcessing(withdrawalAmount, mockPaymentMethod)).thenThrow(new TransactionException());

        withdrawalService.process(mockWithdrawal);

        Withdrawal expectedWithdrawalAfterTransaction = mockWithdrawal(1L, WithdrawalStatus.FAILED);
        expectedWithdrawalAfterTransaction.setPaymentMethodId(withdrawalPaymentMethodId);
        expectedWithdrawalAfterTransaction.setAmount(withdrawalAmount);
        verify(withdrawalRepository, times(2)).save(expectedWithdrawalAfterTransaction);
    }

    @Test
    public void testProcessWithException() throws TransactionException {
        Long withdrawalPaymentMethodId = 2L;
        Double withdrawalAmount = 100d;
        Withdrawal mockWithdrawal = mockWithdrawal(1L, WithdrawalStatus.PENDING);
        mockWithdrawal.setPaymentMethodId(withdrawalPaymentMethodId);
        mockWithdrawal.setAmount(withdrawalAmount);
        PaymentMethod mockPaymentMethod = mockPaymentMethod(withdrawalPaymentMethodId);
        when(paymentMethodService.findById(withdrawalPaymentMethodId)).thenReturn(mockPaymentMethod);
        when(withdrawalProcessingService.sendToProcessing(withdrawalAmount, mockPaymentMethod)).thenThrow(new RuntimeException());

        withdrawalService.process(mockWithdrawal);

        Withdrawal expectedWithdrawalAfterTransaction = mockWithdrawal(1L, WithdrawalStatus.INTERNAL_ERROR);
        expectedWithdrawalAfterTransaction.setPaymentMethodId(withdrawalPaymentMethodId);
        expectedWithdrawalAfterTransaction.setAmount(withdrawalAmount);
        verify(withdrawalRepository, times(2)).save(expectedWithdrawalAfterTransaction);
    }

    @Test
    public void testFindAll() {
        withdrawalService.findAll();
        verify(withdrawalRepository).findAll();
    }

    @Test(expected = NotFoundException.class)
    public void testFindByIdWhenWithdrawalDoesNotExists() {
        Long withdrawalId = 1l;
        when(withdrawalRepository.findById(withdrawalId)).thenThrow(new NotFoundException(""));

        withdrawalService.findById(withdrawalId);
    }

    @Test
    public void testFindByIdWhenWithdrawalExists() {
        Long withdrawalId = 1l;
        when(withdrawalRepository.findById(withdrawalId)).thenReturn(Optional.of(mockWithdrawal(withdrawalId, WithdrawalStatus.PROCESSING)));

        Withdrawal result = withdrawalService.findById(withdrawalId);
        assertEquals(withdrawalId, result.getId());
    }
}
