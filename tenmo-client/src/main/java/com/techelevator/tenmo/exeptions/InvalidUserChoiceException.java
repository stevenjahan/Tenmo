package com.techelevator.tenmo.exeptions;

public class InvalidUserChoiceException  extends Exception {
    public InvalidUserChoiceException(){
        super("Cannot send yourself money. Please select another user.");
    }
}
