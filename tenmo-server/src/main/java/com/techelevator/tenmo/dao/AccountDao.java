package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    static Account getAccountId(Object accountTo) {
    }

    Balance getBalance(String user);
    Account getAccountByUserId(int userId);
    Account getAccountByAccountId(int accountId);

}
