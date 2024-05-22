package org.issk.service;

import org.issk.dto.Session;
import org.issk.dto.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

public interface UserService {
    int createUser(User user);
    Session login(User user);

    ResponseEntity<String> logout(HttpServletRequest request, User user);

    ResponseEntity<String> editPreferences(HttpServletRequest request, User user) ;
    ResponseEntity<String> removePreferences(HttpServletRequest request,User user) ;
    ResponseEntity <String> deleteUser(HttpServletRequest request, User user) ;
    ResponseEntity<String> addFavouriteMovies(HttpServletRequest request, User user);
}
