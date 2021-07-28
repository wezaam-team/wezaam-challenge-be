package com.wezaam.withdrawal.domain;

import com.wezaam.withdrawal.domain.exception.InsufficientAmountException;
import com.wezaam.withdrawal.domain.exception.InvalidPaymentMethodException;
import com.wezaam.withdrawal.domain.exception.InvalidScheduleException;
import com.wezaam.withdrawal.domain.exception.UserDoesNotExistsException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "withdrawals")
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @ManyToOne(targetEntity = PaymentMethod.class)
    private PaymentMethod paymentMethod;

    private BigDecimal amount;

    private Boolean immediate;

    private Instant scheduledFor;

    @Enumerated(EnumType.STRING)
    private WithdrawalStatus withdrawalStatus;

    public Withdrawal() {
        this(null, null, null, null, null, null);
    }

    protected Withdrawal(Long id, User user, PaymentMethod paymentMethod, BigDecimal amount, Instant scheduledFor, Boolean immediate) {
        this(id, user, paymentMethod, amount, scheduledFor, immediate, WithdrawalStatus.PENDING);
    }

    protected Withdrawal(Long id, User user, PaymentMethod paymentMethod, BigDecimal amount, Instant scheduledFor, Boolean immediate, WithdrawalStatus withdrawalStatus) {
        super();
        this.id = id;
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.scheduledFor = scheduledFor;
        this.immediate = immediate;
        this.withdrawalStatus = withdrawalStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getImmediate() {
        return immediate;
    }

    public void setImmediate(Boolean immediate) {
        this.immediate = immediate;
    }

    public Instant getScheduledFor() {
        return scheduledFor;
    }

    public void setScheduledFor(Instant scheduledFor) {
        this.scheduledFor = scheduledFor;
    }

    public WithdrawalStatus getWithdrawalStatus() {
        return withdrawalStatus;
    }

    public void setWithdrawalStatus(WithdrawalStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }

    public void validate() throws
            InsufficientAmountException,
            UserDoesNotExistsException,
            InvalidPaymentMethodException,
            InvalidScheduleException {

        validateUser();
        validatePaymentMethod();
        validateAmount();
        validateSchedule();
    }

    public boolean canBeSent() {
        final boolean isToBeSentImmediately = immediate != null && immediate.booleanValue();

        final boolean isConfiguredToBeSentLater = !isToBeSentImmediately && scheduledFor != null;
        final boolean isToBeSentLaterAndTheTimeHasArrived = isConfiguredToBeSentLater
                && Instant
                .now()
                .isAfter(scheduledFor);

        return isToBeSentImmediately || isToBeSentLaterAndTheTimeHasArrived;
    }

    private void validateAmount() throws InsufficientAmountException {
        if (paymentMethod.getAvailableAmount().compareTo(amount) == -1) {
            throw new InsufficientAmountException();
        }
    }

    private void validatePaymentMethod() throws InvalidPaymentMethodException {
        if (user.getPaymentMethods() == null || user.getPaymentMethods().isEmpty()) {
            throw new InvalidPaymentMethodException();
        }

        if (paymentMethod == null) {
            throw new InvalidPaymentMethodException();
        }

        user.getPaymentMethods()
                .stream()
                .filter(paymentMethod ->
                        paymentMethod.getId().longValue() == this.paymentMethod.getId().longValue())
                .findFirst().orElseThrow(InvalidPaymentMethodException::new);
    }

    private void validateUser() throws UserDoesNotExistsException {
        if (user == null) {
            throw new UserDoesNotExistsException();
        }
    }

    private void validateSchedule() throws InvalidScheduleException {
        boolean isNotToBeExecutedImmediately = (immediate == null || !immediate.booleanValue());
        boolean isNotScheduledToBeExecuted = scheduledFor == null;

        if (isNotToBeExecutedImmediately && isNotScheduledToBeExecuted) {
            throw new InvalidScheduleException();
        }
    }


}
