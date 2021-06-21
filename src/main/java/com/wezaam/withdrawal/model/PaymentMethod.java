package com.wezaam.withdrawal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Setter()
@Getter
@Entity(name = "payment_methods")
public class PaymentMethod {
    public PaymentMethod(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public PaymentMethod(){
        super();
    }
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy="paymentMethod")
    @JsonIgnore
    private List<WithdrawalEntity> withdrawalEntityList;
    @OneToMany(mappedBy="paymentMethod")
    @JsonIgnore
    private List<WithdrawalScheduled> withdrawalScheduledEntityList;
    private String name;
}
