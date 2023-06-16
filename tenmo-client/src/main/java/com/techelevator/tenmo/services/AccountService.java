package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;


public interface AccountService {
    Balance getBalance(AuthenticatedUser authenticatedUser);
    Account getAccountByUserId(AuthenticatedUser authenticatedUser, int userId);
    Account getAccountById(AuthenticatedUser authenticatedUser, int accountID);
}
