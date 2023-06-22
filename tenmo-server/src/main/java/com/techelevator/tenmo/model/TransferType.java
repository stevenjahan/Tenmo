package com.techelevator.tenmo.model;

import org.springframework.data.annotation.Id;

public class TransferType {
    @Id
    private int transferTypeId;
    private String transferDescription;

    public TransferType() { }

    public TransferType(int transferTypeId, String transferDescription) {
        this.transferTypeId = transferTypeId;
        this.transferDescription = transferDescription;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferDescription() {
        return transferDescription;
    }

    public void setTransferDescription(String transferDescription) {
        this.transferDescription = transferDescription;
    }

    @Override
    public String toString() {
        return "TransferType{" +
                "transferTypeId=" + transferTypeId +
                ", transferDescription='" + transferDescription + '\'' +
                '}';
    }
}
