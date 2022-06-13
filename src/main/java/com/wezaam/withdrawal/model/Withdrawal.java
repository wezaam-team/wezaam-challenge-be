package com.wezaam.withdrawal.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity(name = "withdrawals")
public class Withdrawal extends AbstractWithdrawal {

    @Override
    public String toString() {
        return super.toString();
    }
}
