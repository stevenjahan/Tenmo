package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountDao implements AccountDao{

    public static List<Account> accounts = new ArrayList<>();
    private final JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
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
    public Account getAccountById(int id) {
        return null;
    }

    @Override
    public BigDecimal getAccountBalanceById(int id) {
        Account account = null;
        String sql = "select user_id, account_id, balance from account where account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if(results.next()) {
            account = mapRowToAccount(results);
        }
        return account.getBalance();
    }

    @Override
    public BigDecimal getTotalAccountBalance(int id) {
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

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setUserId(rowSet.getInt("user_id"));
        account.setId(rowSet.getInt("account_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
