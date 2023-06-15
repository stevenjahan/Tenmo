package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int getStatusId(int id) {
        int statusId = 0;
        String sql = "SELECT transfer.transfer_status_id " +
                "FROM transfer_status " +
                "JOIN transfer " +
                "ON transfer_status.transfer_status_id = transfer.transfer_status_id " +
                "WHERE transfer_id = ?;";
        try {
            statusId = jdbcTemplate.queryForObject(sql, Integer.class, id);

        } catch (NullPointerException npe) {
            System.err.println("Transfer ID does not exist.");
        }
        return statusId;
    }
}
