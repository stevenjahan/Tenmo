package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getAllTransfers();
    Transfer getTransferById(int id);
    List<Transfer> getAllTransfersForSpecificUser();
    int getTransferAmountByTransferId();
    int getTransferStatus(int id);



}
