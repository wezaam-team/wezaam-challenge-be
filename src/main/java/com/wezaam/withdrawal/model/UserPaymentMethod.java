package com.wezaam.withdrawal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity(name = "USER_PAYMENT_METHODS")
public class UserPaymentMethod {

    @EmbeddedId
    @JsonIgnore
    UserPaymentMethodKey id;

    @JsonBackReference
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id")
    User user;

    @JsonBackReference
    @ManyToOne
    @MapsId("paymentMethodId")
    @JoinColumn(name="payment_method_id")
    PaymentMethod paymentMethod;


    public UserPaymentMethodKey getId() {
        return id;
    }

    public void setId(UserPaymentMethodKey id) {
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


}
