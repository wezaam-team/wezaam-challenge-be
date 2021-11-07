package com.wezaam.withdrawal.service;

import com.google.common.util.concurrent.MoreExecutors;
import com.wezaam.withdrawal.exception.TransactionException;
import com.wezaam.withdrawal.model.*;
import com.wezaam.withdrawal.repository.*;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.*;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {"withdrawal.scheduler.enabled=false"})
public class WithdrawalServiceTest implements WithAssertions {

    private static final Instant NOW =  Instant.parse("2021-11-07T20:00:00Z");

    private final WithdrawalSchedulerService schedulerService = new WithdrawalSchedulerService();

    @Autowired
    private WithdrawalService service;
    @Autowired
    private WithdrawalRepository withdrawalRepository;
    @Autowired
    private WithdrawalScheduledRepository withdrawalScheduledRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @MockBean
    private WithdrawalProcessingService withdrawalProcessingService;
    @MockBean
    private EventsService eventsService;

    @BeforeEach
    private void setup() {
        Clock fixed = Clock.fixed(NOW, ZoneOffset.UTC);
        Clock tick = Clock.tick(fixed, Duration.ofMillis(1));
        service.setExecutorService(MoreExecutors.newDirectExecutorService());
        service.setClock(fixed);
        schedulerService.setWithdrawalService(service);
        schedulerService.setWithdrawalRepository(withdrawalRepository);
        schedulerService.setWithdrawalScheduledRepository(withdrawalScheduledRepository);
        schedulerService.setClock(tick);
    }

    @Test
    public void createAsap() throws Exception {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(BigDecimal.ONE);
        withdrawal.setPaymentMethodId(1L);

        Withdrawal saved = service.create(withdrawal);

        SoftAssertions props = new SoftAssertions();
        props.assertThat(saved.getId()).isNotNull();
        props.assertThat(saved.getAmount()).isOne();
        props.assertThat(saved.getPaymentMethodId()).isOne();
        props.assertThat(saved.getCreatedAt()).isEqualTo(NOW);
        props.assertThat(saved.getStatus()).isSameAs(WithdrawalStatus.PROCESSING);
        props.assertAll();

        ArgumentCaptor<BigDecimal> amountCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<PaymentMethod> paymentMethodCaptor = ArgumentCaptor.forClass(PaymentMethod.class);
        verify(withdrawalProcessingService).sendToProcessing(amountCaptor.capture(), paymentMethodCaptor.capture());
        SoftAssertions args = new SoftAssertions();
        args.assertThat(amountCaptor.getValue()).isEqualByComparingTo(BigDecimal.ONE);
        args.assertThat(paymentMethodCaptor.getValue().getId()).isOne();
        args.assertAll();

        schedulerService.notifyProcessing();

        ArgumentCaptor<Withdrawal> withdrawalCaptor = ArgumentCaptor.forClass(Withdrawal.class);
        verify(eventsService).send(withdrawalCaptor.capture());
        Withdrawal sent = withdrawalCaptor.getValue();
        SoftAssertions sentProps = new SoftAssertions();
        sentProps.assertThat(sent.getId()).isEqualTo(saved.getId());
        sentProps.assertThat(sent.getSentAt()).isEqualTo(NOW);
        sentProps.assertThat(sent.getStatus()).isSameAs(WithdrawalStatus.PROCESSING);
        sentProps.assertAll();

        schedulerService.processPending();
        schedulerService.notifyProcessing();
        verifyNoMoreInteractions(withdrawalProcessingService, eventsService);
    }

    @Test
    public void createScheduled() throws Exception {
        WithdrawalScheduled withdrawal = new WithdrawalScheduled();
        withdrawal.setAmount(BigDecimal.ONE);
        withdrawal.setPaymentMethodId(1L);
        withdrawal.setExecuteAt(NOW.minusSeconds(1));

        WithdrawalScheduled saved = service.schedule(withdrawal);

        SoftAssertions props = new SoftAssertions();
        props.assertThat(saved.getId()).isNotNull();
        props.assertThat(saved.getAmount()).isOne();
        props.assertThat(saved.getPaymentMethodId()).isOne();
        props.assertThat(saved.getCreatedAt()).isEqualTo(NOW);
        props.assertThat(saved.getExecuteAt()).isEqualTo(NOW.minusSeconds(1));
        props.assertThat(saved.getStatus()).isSameAs(WithdrawalStatus.PENDING);
        props.assertAll();

        schedulerService.processPending();

        ArgumentCaptor<BigDecimal> amountCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<PaymentMethod> paymentMethodCaptor = ArgumentCaptor.forClass(PaymentMethod.class);
        verify(withdrawalProcessingService).sendToProcessing(amountCaptor.capture(), paymentMethodCaptor.capture());
        SoftAssertions args = new SoftAssertions();
        args.assertThat(amountCaptor.getValue()).isEqualByComparingTo(BigDecimal.ONE);
        args.assertThat(paymentMethodCaptor.getValue().getId()).isOne();
        args.assertAll();

        schedulerService.notifyProcessing();

        ArgumentCaptor<Withdrawal> withdrawalCaptor = ArgumentCaptor.forClass(Withdrawal.class);
        verify(eventsService).send(withdrawalCaptor.capture());
        Withdrawal sent = withdrawalCaptor.getValue();
        SoftAssertions sentProps = new SoftAssertions();
        sentProps.assertThat(sent.getId()).isEqualTo(saved.getId());
        sentProps.assertThat(sent.getSentAt()).isEqualTo(NOW);
        sentProps.assertThat(sent.getStatus()).isSameAs(WithdrawalStatus.PROCESSING);
        sentProps.assertAll();

        schedulerService.processPending();
        schedulerService.notifyProcessing();
        verifyNoMoreInteractions(withdrawalProcessingService, eventsService);
    }

    @Test
    public void failScheduled() throws Exception {
        WithdrawalScheduled withdrawal = new WithdrawalScheduled();
        withdrawal.setAmount(BigDecimal.ONE);
        withdrawal.setPaymentMethodId(1L);
        withdrawal.setExecuteAt(NOW.minusSeconds(1));

        WithdrawalScheduled saved = service.schedule(withdrawal);

        SoftAssertions savedProps = new SoftAssertions();
        savedProps.assertThat(saved.getId()).isNotNull();
        savedProps.assertThat(saved.getAmount()).isOne();
        savedProps.assertThat(saved.getPaymentMethodId()).isOne();
        savedProps.assertThat(saved.getCreatedAt()).isEqualTo(NOW);
        savedProps.assertThat(saved.getExecuteAt()).isEqualTo(NOW.minusSeconds(1));
        savedProps.assertThat(saved.getStatus()).isSameAs(WithdrawalStatus.PENDING);
        savedProps.assertAll();

        doThrow(TransactionException.class).when(withdrawalProcessingService).sendToProcessing(any(), any());
        schedulerService.processPending();

        ArgumentCaptor<BigDecimal> amountCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<PaymentMethod> paymentMethodCaptor = ArgumentCaptor.forClass(PaymentMethod.class);
        verify(withdrawalProcessingService).sendToProcessing(amountCaptor.capture(), paymentMethodCaptor.capture());
        SoftAssertions args = new SoftAssertions();
        args.assertThat(amountCaptor.getValue()).isEqualByComparingTo(BigDecimal.ONE);
        args.assertThat(paymentMethodCaptor.getValue().getId()).isOne();
        args.assertAll();

        schedulerService.notifyProcessing();

        ArgumentCaptor<Withdrawal> withdrawalCaptor = ArgumentCaptor.forClass(Withdrawal.class);
        verify(eventsService).send(withdrawalCaptor.capture());
        Withdrawal sent = withdrawalCaptor.getValue();
        SoftAssertions sentProps = new SoftAssertions();
        sentProps.assertThat(sent.getId()).isEqualTo(saved.getId());
        sentProps.assertThat(sent.getSentAt()).isEqualTo(NOW);
        sentProps.assertThat(sent.getStatus()).isSameAs(WithdrawalStatus.FAILED);
        sentProps.assertAll();

        schedulerService.processPending();
        schedulerService.notifyProcessing();
        verifyNoMoreInteractions(withdrawalProcessingService, eventsService);
    }
}
