package com.wezaam.withdrawal.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "user")
    private List<PaymentMethod> paymentMethods;

    private double maxWithdrawalAmount;

    protected User() {}

    public User(Long id, String firstName, List<PaymentMethod> paymentMethods, double maxWithdrawalAmount) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setPaymentMethods(paymentMethods);
        this.setMaxWithdrawalAmount(maxWithdrawalAmount);
    }

    public Withdrawal createWithdrawal(long withdrawalId, long paymentMethodId, double amount, Instant executeAt) {
        if (amount > this.getMaxWithdrawalAmount()) {
            throw new IllegalArgumentException(String.format(
                    "Amount: %.2f exceeds max withdrawal amount: %.2f", amount, this.getMaxWithdrawalAmount()));
        }

        if (!this.hasPaymentMethodOfId(paymentMethodId)) {
            throw new IllegalArgumentException(
                    "User of ID: " + this.getId() + " does not have payment method of ID: " + paymentMethodId + ".");
        }

        return new Withdrawal(withdrawalId, this.getId(), paymentMethodId, amount, executeAt);
    }

    public boolean hasPaymentMethodOfId(Long paymentMethodId) {
        return this.paymentMethods
                .stream()
                .anyMatch(paymentMethod -> Objects.equals(paymentMethod.getId(), paymentMethodId));
    }

    public PaymentMethod getPaymentMethodOfId(Long paymentMethodId) {
        return this.paymentMethods
                .stream()
                .filter(paymentMethod -> Objects.equals(paymentMethod.getId(), paymentMethodId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User of ID: " + this.getId()
                        + " does not have payment method of ID: " + paymentMethodId + "."));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    protected void setFirstName(String firstName) {
        if (firstName == null) {
            throw new IllegalArgumentException("The firstName should not be null.");
        }

        this.firstName = firstName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    protected void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods == null ? new ArrayList<>() : paymentMethods;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return this.paymentMethods;
    }

    protected void setMaxWithdrawalAmount(double maxWithdrawalAmount) {
        this.maxWithdrawalAmount = maxWithdrawalAmount;
    }

    public double getMaxWithdrawalAmount() {
        return this.maxWithdrawalAmount;
    }
}
