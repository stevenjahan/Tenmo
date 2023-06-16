package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;

public interface TransferStatusDao {

    int getStatusId(int id);

    TransferStatus getTransferStatusByDesc(String description);

    TransferStatus getTransferStatusById(int transferStatusId);





}
