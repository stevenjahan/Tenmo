package com.techelevator.tenmo.exeptions;

public class InvalidTransferIdChoice extends Exception {
    public InvalidTransferIdChoice() {
        super("Invalid Transfer Id, choose a different Id.");
    }
}
