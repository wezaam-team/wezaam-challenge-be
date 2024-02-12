package com.wezaam.withdrawal.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.math.BigDecimal;

@Entity(name = "payment_methods")
public class PaymentMethod {

    @Id
    private Long id;

    private String name;

    @ManyToOne
    private User user;

    private BigDecimal availableAmount;

    public PaymentMethod() {
        this(null,
                null,
                null,
                null);
    }

    public PaymentMethod(Long id, String name, User user, BigDecimal availableAmount) {
        super();
        this.id = id;
        this.name = name;
        this.user = user;
        this.availableAmount = availableAmount;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }
}
