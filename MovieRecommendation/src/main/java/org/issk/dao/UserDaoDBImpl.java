package org.issk.dao;

import org.issk.dto.User;
import org.issk.mappers.UserMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public User getUserByID(int id) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE uid = ?;", new UserMapper(), id);
        } catch (DataAccessException e){
            return null;
        }
    }

    public User getUserByUsername(String username) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE userName = ?;", new UserMapper(), username);
        } catch (DataAccessException e){
            return null;
        }
    }

    public byte[] enHash(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(text.getBytes());

    }
}
