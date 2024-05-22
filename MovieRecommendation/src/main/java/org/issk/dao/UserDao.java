package org.issk.dao;

import org.issk.dto.Session;
import org.issk.dto.User;
import org.issk.exceptions.InvalidInputException;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

public interface UserDao {
    boolean createUser(User user) throws NoSuchAlgorithmException;

    boolean storeSession(Session session) throws NoSuchAlgorithmException;

    boolean removeSession(Session session);

    boolean checkSessionValid(Session session) throws NoSuchAlgorithmException;

    boolean authenticateUser(User user) throws NoSuchAlgorithmException;

    User getUserByUsername(String username);

    User getUserPreferences(User user);

    Session getSessionById(String sessionId);

    byte[] enHash(String text) throws NoSuchAlgorithmException;

    boolean editPreferences(User user) throws NoSuchAlgorithmException, InvalidInputException;

    boolean removePreferences(User user) throws InvalidInputException;

    boolean deleteUser(User user);

    boolean addFavouriteMovies(User user);

}
