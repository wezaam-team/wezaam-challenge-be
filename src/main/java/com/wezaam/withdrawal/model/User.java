package com.wezaam.withdrawal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String firstName;
    @OneToMany(mappedBy="user")
    private List<PaymentMethod> paymentMethods;
    private Double maxWithdrawalAmount;

}
