package com.wezaam.withdrawal.domain.rest.request;

import com.google.common.base.MoreObjects;
import com.wezaam.withdrawal.model.WithdrawalExecution;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class    WithdrawalRequest implements Serializable {

    private static final long serialVersionUID = 4262465440539315915L;

    @NotNull(message = "Required amount is missing")
    private Double amount;

    @NotEmpty(message ="Required payment method is missing")
    private String paymentMethod;

    @NotEmpty(message = "The user name is missing")
    private String name;

    private Instant executedAt;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Instant executedAt) {
        this.executedAt = executedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawalRequest that = (WithdrawalRequest) o;
        return Objects.equals(amount, that.amount) && Objects.equals(paymentMethod, that.paymentMethod) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, paymentMethod, name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("amount", amount)
                .add("paymentMethod", paymentMethod)
                .add("name", name)
                .toString();
    }
}
