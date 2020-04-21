package com.example.butymovamoneytracker.data.model;

public class BalanceResult {
    private  long total_expenses;
    private long total_income;
    private long balance;

    public long getTotal_expenses() {
        return total_expenses;
    }

    public void setTotal_expenses(long total_expenses) {
        this.total_expenses = total_expenses;
    }

    public long getTotal_income() {
        return total_income;
    }

    public void setTotal_income(long total_income) {
        this.total_income = total_income;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
