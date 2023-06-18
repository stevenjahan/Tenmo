package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;

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

    @Override
    public boolean createAccount(Account newAccount) {
        String sqlNewAccount ="INSERT INTO accounts "
                + "(account_id, user_id, balance) "
                + "VALUES(?,?, ?)";

        newAccount.setAccount_id(getNextAccountId());

        jdbcTemplate.update(sqlNewAccount, newAccount.getAccount_id(), newAccount.getUser_id(), newAccount.getBalance());

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

    @Override
    public List<Account> listAccounts() {
        return null;
    }

    @Override
    public void deleteAccount(int account_id) {

    }

    public BigDecimal getBalanceByUserId(Long user_id) {
        Account returnAccount = new Account();

        String sqlreturnAccount = "SELECT * "
                + "FROM accounts "
                + "WHERE user_id = ? ";

        SqlRowSet accountQuery = jdbcTemplate.queryForRowSet(sqlreturnAccount, user_id);

        if(accountQuery.next()) {
            returnAccount =  mapRowToAccount(accountQuery);
        }

        return returnAccount.getBalance();
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
            newAccount = mapRowToAccount(results);
        }
        return newAccount;
    }

    @Override
    public BigDecimal getAccountBalanceById(int id) {
        return null;
    }

    @Override
    public BigDecimal getTotalAccountBalance(int id) {
        return null;
    }

    @Override
    public Balance getBalance(String user) {
        String sql = "Select balance from accounts join users on accounts.user_id where username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user);
        Balance balance = new Balance();
        if (results.next()) {
            String accountBalance = results.getString("balance");
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
            account = mapRowToAccount(results);
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

        jdbcTemplate.update(sql, accountToUpdate.getBalance(), accountToUpdate.getAccount_id());
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