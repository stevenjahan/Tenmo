package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    @Autowired
    AccountDao AccountDao;
    @Autowired
    UserDao UserDao;
    @Autowired
    TransferDao TransferDao;
    @Autowired
    TransferTypeDao TransferTypeDao;
    @Autowired
    TransferStatusDao TransferStatusDao;

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Balance getBalance(Principal principal) {
        System.out.println(principal.getName());
        return AccountDao.getBalance(principal.getName());
    }

    @RequestMapping(path="/users", method = RequestMethod.GET)
    public List<User> getUsers() {
        return UserDao.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/transfers/{id}", method = RequestMethod.POST)
    public void addTransfer(@RequestBody Transfer transfer, @PathVariable int id) throws InsufficientFunds {

        BigDecimal amountToTransfer = (BigDecimal) transfer.getAmount();
        Account accountFrom = AccountDao.getAccountId(transfer.getAccountFrom());
        Account accountTo = AccountDao.getAccountId(transfer.getAccountTo());

        accountFrom.getBalance().sendMoney(amountToTransfer);
        accountTo.getBalance().receiveMoney(amountToTransfer);

        TransferDao.createTransfer(transfer);

        AccountDao.updateAccount(accountFrom);
        AccountDao.updateAccount(accountTo);
    }

    @RequestMapping(path="/transfertype/filter", method = RequestMethod.GET)
    public TransferType getTransferTypeFromDescription(@RequestParam String description) {
        return TransferDao.getTransferTypeFromDescription(description);
    }

    @RequestMapping(path="/transferstatus/filter", method = RequestMethod.GET)
    public TransferStatus getTransferStatusByDescription(@RequestParam String description) {
        return TransferStatusDao.getTransferStatusByDesc(description);
    }

    @RequestMapping(path="/account/user/{id}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable int id) {
        return AccountDao.getAccountByUserID(id);
    }

    @RequestMapping(path="/account/{id}", method = RequestMethod.GET)
    public Account getAccountFromAccountId(@PathVariable int id) {
        return AccountDao.getAccountByAccountID(id);
    }

    @RequestMapping(path="/transfers/user/{userId}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByUserId(@PathVariable int userId) {
        return TransferDao.getTransfersByUserId(userId);
    }

    @RequestMapping(path="/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {
        return TransferDao.getTransferByTransferId(id);
    }

    @RequestMapping(path="/users/{id}", method = RequestMethod.GET)
    public User getUserByUserId(@PathVariable int id) {
<<<<<<< HEAD
        return UserDao.getUserByUserId(id);
=======
        return userDao.getUserById(id);
>>>>>>> ea7fcfe60209a0558ce77116c6cb6ca3f42c9200
    }

    @RequestMapping(path="/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers() {
        return TransferDao.getAllTransfers();
    }

    @RequestMapping(path="/transfertype/{id}", method = RequestMethod.GET)
    public TransferType getTransferDescFromId(@PathVariable int id)  {
        return TransferTypeDao.getTransferTypeFromId(id);
    }
    @RequestMapping(path="/transferstatus/{id}", method = RequestMethod.GET)
    public TransferStatus getTransferStatusFromId(@PathVariable int id) {
        return TransferStatusDao.getTransferStatusById(id);
    }

    @RequestMapping(path="/transfers/user/{userId}/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfersByUserId(@PathVariable int userId) {
        return TransferDao.getPendingTransfers(userId);
    }

    @RequestMapping(path="/transfers/{id}", method = RequestMethod.PUT)
    public void updateTransferStatus(@RequestBody Transfer transfer, @PathVariable int id) throws InsufficientFunds {

        // only go through with the transfer if it is approved
        if(transfer.getTransferStatusId() == TransferStatusDao.getTransferStatusByDesc("Approved").getTransferStatusId()) {

            BigDecimal amountToTransfer = (BigDecimal) transfer.getAmount();
            Account accountFrom = AccountDao.getAccountByAccountID(transfer.getAccountFrom());
            Account accountTo = AccountDao.getAccountByAccountID(transfer.getAccountTo());

            accountFrom.getBalance().sendMoney(amountToTransfer);
            accountTo.getBalance().receiveMoney(amountToTransfer);

            TransferDao.updateTransfer(transfer);

            AccountDao.updateAccount(accountFrom);
            AccountDao.updateAccount(accountTo);
        } else {
            TransferDao.updateTransfer(transfer);
        }

    }
}