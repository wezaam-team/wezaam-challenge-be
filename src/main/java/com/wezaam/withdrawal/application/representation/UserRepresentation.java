package com.wezaam.withdrawal.application.representation;

import com.wezaam.withdrawal.domain.model.User;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepresentation {

    private Long id;
    private String firstName;
    private List<PaymentMethodRepresentation> paymentMethods;
    private double maxWithdrawalAmount;

    public UserRepresentation(User user) {
        this.setId(user.getId());
        this.setFirstName(user.getFirstName());
        this.setMaxWithdrawalAmount(user.getMaxWithdrawalAmount());

        var paymentMethodRepresentations =
                user.getPaymentMethods().stream().map(PaymentMethodRepresentation::new).collect(Collectors.toList());
        this.setPaymentMethods(paymentMethodRepresentations);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setPaymentMethods(List<PaymentMethodRepresentation> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public List<PaymentMethodRepresentation> getPaymentMethods() {
        return this.paymentMethods;
    }

    public void setMaxWithdrawalAmount(double withdrawalAmount) {
        this.maxWithdrawalAmount = withdrawalAmount;
    }

    public double getMaxWithdrawalAmount() {
        return this.maxWithdrawalAmount;
    }
}
