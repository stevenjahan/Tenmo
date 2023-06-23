package com.techelevator.tenmo.model;


import org.springframework.data.annotation.Id;


public class Account {
    @Id
    private int accountId;
    private int userId;
    private Balance balance;


    public Account() {
    }


    public Account(int accountId, int userId, Balance balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }


    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int id) {
        this.accountId = id;
    }


    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
