package org.issk.dao;


import org.issk.dto.Session;
import org.issk.dto.User;
import org.issk.mappers.SessionMapper;
import org.issk.mappers.UserMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Repository
public class UserDaoDBImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoDBImpl(JdbcTemplate jdbcTemplate) {
        //Connect to DB
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Attempts to add a user to the database
     *
     * @param user user to be persisted
     * @return true if user was persisted, false if user was not persisted
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean createUser(User user) throws NoSuchAlgorithmException{
        //Check if user is in DB
        if (getUserByUsername(user.getUsername()) != null){
            return false;
        }

        //Hash password
        byte[] passwordHash = enHash(user.getPassword());

        //Insert user into database
        int rowsAltered = jdbcTemplate.update("INSERT INTO users (userName, passwordHash) VALUES (?, ?);", user.getUsername(), passwordHash);

        //Check to see if database was altered, return true if so, return false if not
        return rowsAltered>0;
    }

    /**
     * Store a session object in the database
     * @param session session to store
     * @return true if storing successful, false otherwise.
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean storeSession(Session session) throws NoSuchAlgorithmException {
        //Check if session is in DB
        if (getSessionById(session.getSessionId()) != null){
            return false;
        }

        //Insert session into database
        int rowsAltered = jdbcTemplate.update("INSERT INTO sessions (sessionId, userId, sessionStart, sessionPeriod) VALUES (?, ?, ?, ?);",
                session.getSessionId(),
                session.getUser().getUserId(),
                session.getStartTime(),
                session.getPeriodHours());

        //Check to see if database was altered, return true if so, return false if not
        return rowsAltered>0;
    }

    /**
     * Checks to see if the given session is a valid session, within the expiry time
     * @param session session to be validated
     * @return true if the session is still valid, false otherwise
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean checkSessionValid(Session session) throws NoSuchAlgorithmException{
        //Get session in database with matching ID
        Session storedSession = getSessionById(session.getSessionId());
        if (storedSession == null){
            return false;
        }
        //Check if it's expired
        if (storedSession
                .getStartTime()
                .plusHours(storedSession.getPeriodHours())
                .isBefore(LocalDateTime.now())) {
            return false;
        }
        //Otherwise, return true;
        return true;
    }

    /**
     * Checks to see if hash of password matches the one in the database
     * @param user user to be authenticated
     * @return true if the user is valid, false otherwise
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean authenticateUser(User user) throws NoSuchAlgorithmException {
        //Hash password
        byte[] passwordHash = enHash(user.getPassword());

        //Find user in database
        try {
            User dbUser = jdbcTemplate.queryForObject("SELECT * FROM users WHERE userName = ? AND passwordHash = ?;", new UserMapper(), user.getUsername(), passwordHash);
        } catch (DataAccessException e){
            return false;
        }
        return true;
    }

    public User getUserByID(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE uid = ?;", new UserMapper(), id);
        } catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE userName = ?;", new UserMapper(), username);
        } catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public Session getSessionById(String sessionId) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM sessions WHERE sessionId = ?;", new SessionMapper(), sessionId);
        } catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public byte[] enHash(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(text.getBytes());
    }
}
