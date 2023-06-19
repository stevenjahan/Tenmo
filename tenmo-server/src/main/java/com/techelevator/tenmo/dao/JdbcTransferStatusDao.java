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
                "FROM transfer_status " +
                "WHERE transfer_status_desc = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, description);

        TransferStatus transferStatus = null;

        if (result.next()) {
            int tempTransferStatusId = result.getInt("transfer_status_id");
            String tempTransferStatusDesc = result.getString("transfer_status_desc");
            transferStatus = new TransferStatus(tempTransferStatusId, tempTransferStatusDesc);

        }

        return transferStatus;
    }

    @Override
    public TransferStatus getTransferStatusById(int transferStatusId) {
        TransferStatus transferStatus = null;

        String sql = "SELECT transfer_status_id, transfer_status_desc " +
                "FROM transfer_status " +
                "WHERE transfer_status_id = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferStatusId);

        if(result.next()) {
            int tempTransferStatusId = result.getInt("transfer_status_id");
            String tempTransferStatusDesc = result.getString("transfer_status_desc");
            transferStatus = new TransferStatus(tempTransferStatusId, tempTransferStatusDesc);
        }
        return transferStatus;
    }
}
