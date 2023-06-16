package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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

    @Override
    public TransferStatus getTransferStatusByDesc(String description) {
        String sql = "SELECT transfer_status_id, transfer_status_desc " +
                "FROM transfer_statuses " +
                "WHERE transfer_status_desc = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, description);

        TransferStatus transferStatus = null;

        if (result.next()) {
            int transferStatusID = result.getInt("transfer_status_id");
            String transferStatusDesc = result.getString("transfer_status_desc");
            transferStatus = new TransferStatus(transferStatusID, transferStatusDesc);

        }

        return transferStatus;
    }
}
