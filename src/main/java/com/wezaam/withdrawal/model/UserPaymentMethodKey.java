package com.wezaam.withdrawal.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserPaymentMethodKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "payment_method_id")
    Long paymentMethodId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPaymentMethodKey that = (UserPaymentMethodKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(paymentMethodId, that.paymentMethodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, paymentMethodId);
    }
}
