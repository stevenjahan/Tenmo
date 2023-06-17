package com.techelevator.tenmo.exeptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(){
        super("User not found, enter a valid user ID.");
    }
}
