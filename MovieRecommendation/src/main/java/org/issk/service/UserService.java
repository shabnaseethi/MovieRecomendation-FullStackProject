package org.issk.service;

import org.issk.dto.Session;
import org.issk.dto.User;

public interface UserService {
    int createUser(User user);
    Session login(User user);
}
