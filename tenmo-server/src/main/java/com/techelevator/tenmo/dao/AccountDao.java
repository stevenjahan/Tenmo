package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    Balance getBalance(String user);
    Account getAccountByUserId(int userId);
    Account getAccountByAccountId(int accountId);

    List<Account> getAllAccounts();

    List<Account> getAccountsToEachUserId();

    Account getAccountById(int id);

    BigDecimal getAccountBalanceById(int id);

    BigDecimal getTotalAccountBalance(int id);

    boolean createAccount(Account account);
    boolean updateAccount(Account account);
    boolean deleteAccount(Account account);

    List<Account> listAccounts();

    void deleteAccount(int account_id);
}
