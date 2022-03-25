package com.wezaam.withdrawal.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String firstName;

    @Cascade(CascadeType.DELETE)
    @JsonManagedReference
    @OneToMany(mappedBy="user")
    private Set<UserPaymentMethod> userPaymentMethods;
    private Double maxWithdrawalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Set<UserPaymentMethod> getUserPaymentMethods() {
        return userPaymentMethods;
    }

    public void setUserPaymentMethods(Set<UserPaymentMethod> userPaymentMethods) {
        this.userPaymentMethods = userPaymentMethods;
    }

    public Double getMaxWithdrawalAmount() {
        return maxWithdrawalAmount;
    }

    public void setMaxWithdrawalAmount(Double maxWithdrawalAmount) {
        this.maxWithdrawalAmount = maxWithdrawalAmount;
    }
}
