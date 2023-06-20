package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {
    @Autowired
    TransferDao TransferDao;

    @Autowired
    AccountDao AccountDao;

    @Autowired
    TransferStatusDao TransferStatusDao;


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public void addTransfer(@RequestBody Transfer transfer) throws InsufficientFunds {

        BigDecimal amountToTransfer = (BigDecimal) transfer.getAmount();
        Account accountFrom = AccountDao.getAccountByAccountId(transfer.getAccountFrom());
        Account accountTo = AccountDao.getAccountByAccountId(transfer.getAccountTo());

        accountFrom.getBalance().sendMoney(amountToTransfer);
        accountTo.getBalance().receiveMoney(amountToTransfer);

        TransferDao.createTransfer(transfer);

        AccountDao.updateAccount(accountFrom);
        AccountDao.updateAccount(accountTo);
    }

    @RequestMapping(path = "/transfer/tenmo_user/{userId}", method = RequestMethod.GET)
    public List<Transfer> getTransfersFromUserId(@PathVariable int userId) {
        return TransferDao.getTransfersByUserId(userId);
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {
        return TransferDao.getTransferByTransferId(id);
    }


    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers() {
        return TransferDao.getAllTransfers();
    }


    @RequestMapping(path = "/transfer/tenmo_user/{userId}/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfersByUserId(@PathVariable int userId) {
        return TransferDao.getPendingTransfers(userId);
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.PUT)
    public void updateTransferStatus(@RequestBody Transfer transfer, @PathVariable int id) throws InsufficientFunds {

        // only go through with the transfer if it is approved
        if (transfer.getTransferStatusId() == TransferStatusDao.getTransferStatusByDesc("Approved").getTransferStatus()) {

            BigDecimal amountToTransfer = (BigDecimal) transfer.getAmount();
            Account accountFrom = AccountDao.getAccountByAccountId(transfer.getAccountFrom());
            Account accountTo = AccountDao.getAccountByAccountId(transfer.getAccountTo());

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
