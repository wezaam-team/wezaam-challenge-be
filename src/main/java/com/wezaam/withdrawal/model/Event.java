package com.wezaam.withdrawal.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "failed_events")
public class Event {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "withdrawal_id", referencedColumnName = "id")
    private Withdrawal withdrawal;

    @OneToOne
    @JoinColumn(name = "withdrawal_scheduled_id", referencedColumnName = "id")
    private WithdrawalScheduled withdrawalScheduled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Withdrawal getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(Withdrawal withdrawal) {
        this.withdrawal = withdrawal;
    }

    public WithdrawalScheduled getWithdrawalScheduled() {
        return withdrawalScheduled;
    }

    public void setWithdrawalScheduled(WithdrawalScheduled withdrawalScheduled) {
        this.withdrawalScheduled = withdrawalScheduled;
    }
}
