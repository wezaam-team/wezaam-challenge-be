package com.wezaam.withdrawal.domain;

import javax.persistence.*;

import java.util.List;

@Entity(name = "users")
public class User {

    @Id
    private Long id;

    private String name;

    @OneToMany(targetEntity = PaymentMethod.class, mappedBy = "user")
    private List<PaymentMethod> paymentMethods;

    public User() {
        this(null,
                null,
                null);
    }

    public User(Long id, String name, List<PaymentMethod> paymentMethods) {
        super();
        this.id = id;
        this.name = name;
        this.paymentMethods = paymentMethods;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
