package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferTypeDao implements TransferTypeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public TransferType getTransferTypeFromDescription(String description) {
        TransferType transferType = null;

        String sql = "SELECT transfer_type_id, transfer_type_desc " +
                "FROM transfer_type " +
                "WHERE transfer_type_desc = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, description);

        if(result.next()) {
            int tempTransferTypeId = result.getInt("transfer_type_id");
            String tempTransferTypeDesc = result.getString("transfer_type_desc");
            transferType = new TransferType(tempTransferTypeId, tempTransferTypeDesc);
        }

        return transferType;
    }

    @Override
    public TransferType getTransferTypeFromId(int id) {
        TransferType transferType = null;

        String sql = "SELECT transfer_type_id, transfer_type_desc " +
                "FROM transfer_type " +
                "WHERE transfer_type_id = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);

        if(result.next()) {
            int tempTransferTypeId = result.getInt("transfer_type_id");
            String tempTransferTypeDesc = result.getString("transfer_type_desc");
            transferType = new TransferType(tempTransferTypeId, tempTransferTypeDesc);
        }

        return transferType;
    }


}
