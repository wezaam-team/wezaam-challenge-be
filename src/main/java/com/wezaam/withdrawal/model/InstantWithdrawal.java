package com.wezaam.withdrawal.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@AllArgsConstructor
@Entity(name = "withdrawals")
public class InstantWithdrawal extends Withdrawal{
}
