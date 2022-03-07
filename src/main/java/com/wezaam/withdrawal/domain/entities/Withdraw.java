package com.wezaam.withdrawal.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "withdraws")
public class Withdraw {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate executionDate;

    @Enumerated(EnumType.STRING)
    private WithdrawStatusEnum status;

    private BigDecimal amount;

    private String currency;

    @ManyToOne()
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    private String paymentMethod;

}
