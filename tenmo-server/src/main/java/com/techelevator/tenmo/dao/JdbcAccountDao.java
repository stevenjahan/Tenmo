package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import java.util.List;

import javax.sql.DataSource;

import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
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




    public Balance getBalanceByUserId(int userId) {
        Account returnAccount = new Account();

        String sqlReturnAccount = "SELECT * "
                + "FROM account "
                + "WHERE user_id = ? ";

        SqlRowSet accountQuery = jdbcTemplate.queryForRowSet(sqlReturnAccount, userId);

        if(accountQuery.next()) {
            returnAccount =  mapResultsToAccount(accountQuery);
        }

        return returnAccount.getBalance();
    }
    @Override
    public Account getAccountId(int accountId) {
        Account newAccount = new Account();
        String sqlGetAccount = "SELECT * "
                + "FROM account "
                + "WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAccount, accountId);
        if (results.next()) {
            newAccount = mapResultsToAccount(results);
        }
        return newAccount;
    }




    @Override
    public Balance getBalance(String user) {
        String sql = "Select balance from account join tenmo_user on account.user_id = tenmo_user.user_id where username = ?;";
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
        String sql = "Select account_id, user_id, balance from account where user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        Account account = null;
        if (results.next()) {
            account = mapResultsToAccount(results);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        String sql = "Select account_id, user_id, balance from account where account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        Account account = null;
        if (results.next()) {
            account = mapResultsToAccount(results);
        }
        return account;
    }



    @Override
    public Account updateAccount(Account accountToUpdate) {
        String sql = "Update account " +
            "Set balance = ? " +
            "where account_id = ?;";

        jdbcTemplate.update(sql, accountToUpdate.getBalance().getBalance(), accountToUpdate.getAccountId());
        return accountToUpdate;
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