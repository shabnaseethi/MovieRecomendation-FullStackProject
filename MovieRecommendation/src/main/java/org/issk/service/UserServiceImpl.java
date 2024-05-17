package org.issk.service;

import org.issk.dao.UserDao;
import org.issk.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    @Autowired
    UserDao userDao;

    public UserServiceImpl(UserDao userDao) {this.userDao = userDao;}

    public void createUser(User user){
        System.out.println(userDao.createUser(user));
    }
}
