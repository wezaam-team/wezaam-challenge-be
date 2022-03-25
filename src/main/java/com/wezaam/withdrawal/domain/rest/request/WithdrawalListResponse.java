package com.wezaam.withdrawal.domain.rest.request;

import com.wezaam.withdrawal.model.Withdrawal;
import com.wezaam.withdrawal.model.WithdrawalScheduled;

import java.io.Serializable;
import java.util.List;

public class WithdrawalListResponse implements Serializable {

    private static final long serialVersionUID = -4178913618709163485L;

    private List<Withdrawal> withdrawal;
    private List<WithdrawalScheduled> withdrawalsScheduled;

    public WithdrawalListResponse(List<Withdrawal> withdrawal, List<WithdrawalScheduled> withdrawalsScheduled) {
        this.withdrawal = withdrawal;
        this.withdrawalsScheduled = withdrawalsScheduled;
    }

    public List<Withdrawal> getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(List<Withdrawal> withdrawal) {
        this.withdrawal = withdrawal;
    }

    public List<WithdrawalScheduled> getWithdrawalsScheduled() {
        return withdrawalsScheduled;
    }

    public void setWithdrawalsScheduled(List<WithdrawalScheduled> withdrawalsScheduled) {
        this.withdrawalsScheduled = withdrawalsScheduled;
    }
}
