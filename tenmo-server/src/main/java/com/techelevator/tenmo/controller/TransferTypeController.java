package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferTypeDao;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferTypeController {
    @Autowired
    TransferTypeDao TransferTypeDao;

    @RequestMapping(path="/transfer_type/filter/{description}", method = RequestMethod.GET)
    public TransferType getTransferTypeFromDescription(@PathVariable String description) {
        return TransferTypeDao.getTransferTypeFromDescription(description);
    }

    @RequestMapping(path="/transfer_type/{id}", method = RequestMethod.GET)
    public TransferType getTransferDescFromId(@PathVariable int id)  {
        return TransferTypeDao.getTransferTypeFromId(id);
    }
}
