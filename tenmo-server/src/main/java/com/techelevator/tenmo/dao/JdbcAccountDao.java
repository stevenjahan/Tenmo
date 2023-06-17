package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;

import javax.sql.DataSource;

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
    public List<Account> listAccounts() {
        List<Account> returnList = new ArrayList<Account>();

        String sqlListAccounts = "SELECT * "
                + "FROM accounts";


        SqlRowSet accountQuery = jdbcTemplate.queryForRowSet(sqlListAccounts);

        while(accountQuery.next()) {
            Account theAccount =  mapRowToAccount(accountQuery);
            returnList.add(theAccount);
        }

        return returnList;
    }

    public BigDecimal addToBalance(double amount_to_add, int account_id) {

        Account updateAccount = getAccountById(account_id);

        BigDecimal newBalance = updateAccount.getBalance();

        String sqlUpdateAccount = "UPDATE accounts "
                + "SET balance = ? WHERE account_id = ?";

        jdbcTemplate.update(sqlUpdateAccount, newBalance, account_id);

        return newBalance;
    }

    public BigDecimal subtractFromBalance(double amount_to_sub, int account_id) {

        Account updateAccount = getAccountById(account_id);

        BigDecimal newBalance = updateAccount.getBalance();

        String sqlUpdateAccount = "UPDATE accounts "
                + "SET balance = ? WHERE account_id = ?";

        jdbcTemplate.update(sqlUpdateAccount, newBalance, account_id);

        return newBalance;
    }

    @Override
    public void deleteAccount(int account_id) {
        String sqlDeleteAccount = "DELETE from accounts where account_id = ? ";

        jdbcTemplate.update(sqlDeleteAccount, account_id);

    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account theAccount = new Account();

        theAccount.setAccount_id(results.getLong("account_id"));
        theAccount.setUser_id(results.getInt("user_id"));
        theAccount.setBalance(BigDecimal.valueOf(results.getDouble("balance")));

        return theAccount;
    }

    private long getNextAccountId() {
        SqlRowSet nextAccountIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_account_id')");

        if(nextAccountIdResult.next()) {
            return nextAccountIdResult.getLong(1);
        }else {
            throw new RuntimeException ("Something went wrong while getting an id for the new transfer");
        }
    }

}