package com.example.butymovamoneytracker.data.db;

public class BalanceEntity {

    private Long total_expense;
    private Long total_income;
    private Long balance;

    public Long getTotal_expense() {
        return total_expense;
    }

    public void setTotal_expense(Long total_expense) {
        this.total_expense = total_expense;
    }

    public Long getTotal_income() {
        return total_income;
    }

    public void setTotal_income(Long total_income) {
        this.total_income = total_income;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
