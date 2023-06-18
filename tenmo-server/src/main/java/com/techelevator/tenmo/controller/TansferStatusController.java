package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("isAuthenticated()")
public class TansferStatusController {

    @Autowired
    TransferStatusDao TransferStatusDao;

    @RequestMapping(path="/transfer_status/filter", method = RequestMethod.GET)
    public TransferStatus getTransferStatusByDescription(@RequestParam String description) {
        return TransferStatusDao.getTransferStatusByDesc(description);
    }
    @RequestMapping(path="/transfer_status/{id}", method = RequestMethod.GET)
    public TransferStatus getTransferStatusFromId(@PathVariable int id) {
        return TransferStatusDao.getTransferStatusById(id);
    }

}
