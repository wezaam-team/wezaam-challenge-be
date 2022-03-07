package com.wezaam.withdrawal.domain.entities;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Entity(name = "employees")
@Data
public class Employee {

    @Id
    private String employeeId;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    private List<PaymentMethod> paymentMethods;

    @OneToMany(mappedBy = "employee")
    private List<Withdraw> withdraws;

    private LocalDate createdDate;

    private String email;

    private BigDecimal totalBalance;

    private String currency;

}
