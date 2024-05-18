package org.issk.dao;

import org.issk.dto.User;

import java.security.NoSuchAlgorithmException;

public interface UserDao {
    boolean createUser(User user) throws NoSuchAlgorithmException;
}
