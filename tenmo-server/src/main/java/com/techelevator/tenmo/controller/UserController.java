package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    UserDao UserDao;


    @RequestMapping(path="/tenmo_user", method = RequestMethod.GET)
    public List<User> getUsers() {
        return UserDao.findAll();
    }

    @RequestMapping(path="/tenmo_user/{id}", method = RequestMethod.GET)
    public User getUserByUserId(@PathVariable int id) {

        return UserDao.getUserById(id);

    }
}
