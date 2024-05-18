package org.issk.service;

import org.issk.dao.UserDao;
import org.issk.dto.Session;
import org.issk.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;


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

    @Override
    public Session login(User user){
        //Check to see if the username and password match one in the database
        boolean validUser;
        try { validUser = userDao.authenticateUser(user); } catch (NoSuchAlgorithmException e) { return null; }

        //Get user's id in database
        user.setUserId(userDao.getUserByUsername(user.getUsername()).getUserId());

        if (validUser){
            //Get single instance of current time
            LocalDateTime now = LocalDateTime.now();

            //Create sessionHash of username and current time
            //Turns the byte array from enHash into a string
            String sessionId;
            try { sessionId = Hex.encodeHexString(userDao.enHash(user.getUsername() + now)); }
            catch (NoSuchAlgorithmException e) { return null; }

            //Create new session
            Session session = new Session();
            session.setUser(user);
            session.setStartTime(now);
            session.setPeriodHours(1);
            session.setSessionId(sessionId);

            //Store the session in the database
            try { userDao.storeSession(session); } catch (NoSuchAlgorithmException e) { return null; }

            //Return it to the caller
            return session;
        }
        return null;
    }
}
