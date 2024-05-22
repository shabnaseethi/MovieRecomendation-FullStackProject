package org.issk.service;

import org.issk.dao.UserDao;
import org.issk.dto.Genre;
import org.issk.dto.Session;
import org.issk.dto.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class UserDaoDBStubImpl implements UserDao {

    public User user;
    public Genre genre;

    public UserDaoDBStubImpl() {
        user = new User();
        user.setUsername("Sam");
        user.setPassword("Sam12345");

    }


    @Override
    public boolean createUser(User user) throws NoSuchAlgorithmException {
        return false;
    }

    @Override
    public boolean storeSession(Session session) throws NoSuchAlgorithmException {
        return false;
    }

    @Override
    public boolean checkSessionValid(Session session) throws NoSuchAlgorithmException {
        return false;
    }

    @Override
    public boolean authenticateUser(User user) throws NoSuchAlgorithmException {
        return false;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public User getUserPreferences(User user) {
        return null;
    }

    @Override
    public Session getSessionById(String sessionId) {
        return null;
    }

    @Override
    public byte[] enHash(String text) throws NoSuchAlgorithmException {
        return new byte[0];
    }

    @Override
    public boolean editPreferences(User user) throws NoSuchAlgorithmException {
        genre = new Genre();
        genre.setGenreId(12);
        genre.setName("Action");

        HashMap<Integer,Genre> preferredGenre = new HashMap<>();
        preferredGenre.put(1,genre);
        user.setPreferredGenres(preferredGenre);

        return true;
    }

    @Override
    public boolean removePreferences(User user) {
        return false;
    }

    @Override
    public boolean deleteUser(User user) {
        return false;
    }

    @Override
    public boolean addFavouriteMovies(User user) {
        return false;
    }

}
