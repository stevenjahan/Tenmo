package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;

public interface TransferTypeService {

    TransferType getTransferType(AuthenticatedUser authenticatedUser, String description);
    TransferType getTransferTypeFromId(AuthenticatedUser authenticatedUser, int transferTypeId);
}
