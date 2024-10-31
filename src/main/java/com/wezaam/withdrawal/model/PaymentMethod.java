package com.wezaam.withdrawal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    //@JsonManagedReference
    @OneToMany(mappedBy = "paymentMethod")
    @JsonIgnore
    private Set<UserPaymentMethod> userPaymentMethodList;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UserPaymentMethod> getUserPaymentMethodList() {
        return userPaymentMethodList;
    }

    public void setUserPaymentMethodList(Set<UserPaymentMethod> userPaymentMethodList) {
        this.userPaymentMethodList = userPaymentMethodList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
