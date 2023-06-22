package com.techelevator.tenmo.model;

import org.springframework.data.annotation.Id;

public class TransferStatus {

    @Id
    private int transferStatus;
    private String transferStatusDesc;

    public TransferStatus() { }

    public TransferStatus(int transferStatus, String transferStatusDesc) {
        this.transferStatus = transferStatus;
        this.transferStatusDesc = transferStatusDesc;
    }

    public int getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    @Override
    public String toString() {
        return "TransferStatus{" +
                "transferStatus=" + transferStatus +
                ", transferStatusDesc='" + transferStatusDesc + '\'' +
                '}';
    }

}
