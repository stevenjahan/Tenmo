package com.techelevator.tenmo.model;

public class Account {

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


    public static int getAccountId(int accountId) {
        return accountId;
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

    public int getUserId(int userId) {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
