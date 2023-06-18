package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import java.util.List;

import javax.sql.DataSource;

import com.techelevator.tenmo.model.Balance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;

@Component
class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }




    public BigDecimal getBalanceByUserId(Long user_id) {
        Account returnAccount = new Account();

        String sqlReturnAccount = "SELECT * "
                + "FROM accounts "
                + "WHERE user_id = ? ";

        SqlRowSet accountQuery = jdbcTemplate.queryForRowSet(sqlReturnAccount, user_id);

        if(accountQuery.next()) {
            returnAccount =  mapResultsToAccount(accountQuery);
        }

        return returnAccount.getBalance().getBalance();
    }

    @Override
    public List<Account> getAllAccounts() {
        return null;
    }

    @Override
    public List<Account> getAccountsToEachUserId() {
        return null;
    }

    @Override
    public Account getAccountById(int account_id) {
        Account newAccount = new Account();
        String sqlGetAccount = "SELECT * "
                + "FROM accounts "
                + "WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAccount, account_id);
        if (results.next()) {
            newAccount = mapResultsToAccount(results);
        }
        return newAccount;
    }


    @Override
    public Balance getBalance(String user) {
        String sql = "Select balance from accounts join users on accounts.user_id where username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user);
        Balance balance = new Balance();
        if (results.next()) {
            String accountBalance = results.getString("balance");
            assert accountBalance != null;
            balance.setBalance(new BigDecimal(accountBalance));
        }
        return balance;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        String sql = "Select account_id, user_id, balance from accounts where user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        Account account = null;
        if (results.next()) {
            account = mapResultsToAccount(results);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        String sql = "Select account_id, user_id, balance from accounts where account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        Account account = null;
        if (results.next()) {
            account = mapResultsToAccount(results);
        }
        return account;
    }

    @Override
    public void updateAccount(Account accountToUpdate) {
        String sql = "Update accounts " +
            "Set balance = ? " +
            "where account_id = ?;";

        jdbcTemplate.update(sql, accountToUpdate.getBalance(), accountToUpdate.getAccountId(transfer.getAccountTo()));
    }

    private Account mapResultsToAccount(SqlRowSet results) {
        int accountId = results.getInt("account_id");
        int userAccountId = results.getInt("user_id");
        Balance balance = new Balance();
        String accountBalance = results.getString("balance");
        balance.setBalance(new BigDecimal(accountBalance));
        return new Account(accountId, userAccountId, balance);
    }



}