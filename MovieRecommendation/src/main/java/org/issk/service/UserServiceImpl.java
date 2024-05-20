package org.issk.service;

import org.issk.dao.UserDao;
import org.issk.dto.Session;
import org.issk.dto.User;
import org.issk.exceptions.InvalidSessionException;
import org.issk.exceptions.SessionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Hex;

import javax.servlet.http.HttpServletRequest;
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

    @Override
    public ResponseEntity<String> editPreferences(HttpServletRequest request, User user){

        try {
            Session session = checkValidUser(request, user); // To check validity of session and user

            user.setUserId(session.getUser().getUserId());

            if(userDao.editPreferences(user)) return ResponseEntity.ok("Preferences edited successfully");
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to edit preferences:Preference already exists");
        } catch (InvalidSessionException | SessionNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid session or session not found"); // Invalid session or session not found
        }
        catch (NoSuchAlgorithmException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }

    }

    @Override
    public ResponseEntity<String> removePreferences(HttpServletRequest request, User user) {
        try {
            Session session = checkValidUser(request, user);
            user.setUserId(session.getUser().getUserId());
            if (userDao.removePreferences(user)) {
                return ResponseEntity.ok("Preferences removed successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to remove preferences");
            }
        } catch (InvalidSessionException | SessionNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid session or session not found");
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @Override
    public ResponseEntity<String> deleteUser(HttpServletRequest request, User user) {
        try {
            Session session = checkValidUser(request, user);
            user.setUserId(session.getUser().getUserId());
            if (userDao.deleteUser(user)) {
                return ResponseEntity.ok("User removed successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to remove user");
            }
        } catch (InvalidSessionException | SessionNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid session or session not found");
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    public Session checkValidUser(HttpServletRequest request,User user) throws NoSuchAlgorithmException, InvalidSessionException, SessionNotFoundException {

//        Get sessionID from the header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidSessionException("Invalid session ID");
        }

        String sessionId = authorizationHeader.substring("Bearer ".length());
        Session session = userDao.getSessionById(sessionId);

        if (session == null) {
            throw new SessionNotFoundException("Session not found");
        }

        if (!session.getUser().getUsername().equals(user.getUsername())) {
            throw new InvalidSessionException("Session does not match the username");
        }

        if (!userDao.checkSessionValid(session)) {
            throw new InvalidSessionException("Session is not valid");
        }

        return session;
    }

}
