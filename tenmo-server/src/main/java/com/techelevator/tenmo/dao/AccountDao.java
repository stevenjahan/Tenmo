package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

    List<Account> getAllAccounts();

    List<Account> getAccountsToEachUserId();

    Account getAccountById(int id);

    double getAccountBalanceById(int id);

    double getTotalAccountBalance(int id);

    boolean createAccount(Account account);
    boolean updateAccount(Account account);
    boolean deleteAccount(Account account);
}
