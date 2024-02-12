package com.weezam.challenge.user.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @OneToMany(mappedBy="user")
    private List<PaymentMethodEntity> paymentMethods;

    @Column(name = "max_withdrawal_amount")
    private Double maxWithdrawalAmount;
}
