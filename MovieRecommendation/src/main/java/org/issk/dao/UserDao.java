package org.issk.dao;

import org.issk.dto.Session;
import org.issk.dto.User;

import java.security.NoSuchAlgorithmException;

public interface UserDao {
    boolean createUser(User user) throws NoSuchAlgorithmException;

    boolean storeSession(Session session) throws NoSuchAlgorithmException;

    boolean checkSessionValid(Session session) throws NoSuchAlgorithmException;

    boolean authenticateUser(User user) throws NoSuchAlgorithmException;

    User getUserByUsername(String username);

    byte[] enHash(String text) throws NoSuchAlgorithmException;
}
