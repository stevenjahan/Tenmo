package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    void createTransfer(Transfer transfer);

    List<Transfer> getTransfersByUserId(int userId);

    Transfer getTransferByTransferId(int transferId);

    List<Transfer> getAllTransfers();
    Transfer getTransferById(int id);
    List<Transfer> getAllTransfersForSpecificUser();
    int getTransferAmountByTransferId();
    int getTransferStatus(int id);


    List<Transfer> getPendingTransfers(int userId);

    void updateTransfer(Transfer transfer);
}
