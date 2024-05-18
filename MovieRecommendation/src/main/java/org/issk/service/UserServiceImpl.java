package org.issk.service;

import org.issk.dao.UserDao;
import org.issk.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userDao;

    public UserServiceImpl(UserDao userDao) {this.userDao = userDao;}

    @Override
    public int createUser(User user){
        //TODO better validation
        if ((!user.getUsername().isEmpty())&&(user.getPassword().length()>=8)){
            try {
                return userDao.createUser(user) ? 204 : 500;
            } catch (NoSuchAlgorithmException e){
                return 500;
            }
        } else {
            return 400;
        }
    }
}
