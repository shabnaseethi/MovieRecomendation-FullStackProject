package org.issk.dao;

import org.issk.dto.User;

import java.util.List;

public interface UserDao {
    List<User> createUser(User user);
}
