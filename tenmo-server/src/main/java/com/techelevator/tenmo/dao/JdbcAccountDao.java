package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public class JdbcAccountDao implements AccountDao{


    @Override
    public List<Account> getAllAccounts() {
        return null;
    }

    @Override
    public List<Account> getAccountsToEachUserId() {
        return null;
    }

    @Override
    public Account getAccountById(int id) {
        return null;
    }

    @Override
    public double getAccountBalanceById(int id) {
        return 0;
    }

    @Override
    public double getTotalAccountBalance(int id) {
        return 0;
    }

    @Override
    public boolean createAccount(Account account) {
        return false;
    }

    @Override
    public boolean updateAccount(Account account) {
        return false;
    }

    @Override
    public boolean deleteAccount(Account account) {
        return false;
    }
}
