package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getAllTransfers() {

        return null;
    }

    @Override
    public Transfer getTransferById(int id) {
        return null;
    }

    @Override
    public List<Transfer> getAllTransfersForSpecificUser() {
        return null;
    }

    @Override
    public int getTransferAmountByTransferId() {
        return 0;
    }

    @Override
    public int getTransferStatus(int id) {
        return 0;
    }
}
