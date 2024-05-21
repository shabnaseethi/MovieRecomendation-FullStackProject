package org.issk.dao;

import org.issk.dto.Genre;
import org.issk.dto.Session;
import org.issk.dto.User;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserDaoStubImpl implements UserDao {

    public User onlyUser;
    public Session onlySession;
    public Genre onlyGenre;

    public UserDaoStubImpl() {
        onlyGenre = new Genre();
        onlyGenre.setGenreId(999);
        onlyGenre.setName("Tachyonesque");

        onlyUser = new User();
        onlyUser.setUserId(999);
        onlyUser.setUsername("Personson");
        onlyUser.setPassword("baklava");
        onlyUser.setPreferredGenres((HashMap)Map.of(999, onlyGenre));

        onlySession = new Session();
        onlySession.setSessionId("testSessionId");
        onlySession.setUser(onlyUser);
        onlySession.setStartTime(LocalDateTime.MIN);
        onlySession.setPeriodHours(9999999);
    }

    @Override
    public boolean createUser(User user) throws NoSuchAlgorithmException {
        if (user.getUsername().isEmpty() || user.getPassword().length()<8){
            return false;
        }
        onlyUser = user;
        return true;
    }

    @Override
    public boolean storeSession(Session session) throws NoSuchAlgorithmException {
        if (session.getSessionId() == onlySession.getSessionId()){
            return false;
        }
        onlySession = session;
        return true;
    }

    @Override
    public boolean checkSessionValid(Session session) throws NoSuchAlgorithmException {
        if (!session.getSessionId().equals(onlySession.getSessionId())){
            return false;
        }

        //Checks to see if the expiry for the given session is before the expiry for the onlySession
        if (session.getStartTime().plusHours(session.getPeriodHours()).isBefore(onlySession.getStartTime().plusHours(onlySession.getPeriodHours()))){
            //This would be a millennium out...
            return false;
        }
        return false;
    }

    /**
     * Password: baklava
     * @param user
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean authenticateUser(User user) throws NoSuchAlgorithmException {
        if (user.getPassword().equals(onlyUser.getPassword())) {
            return true;
        }
        return false;
    }

    /**
     * returns onlyUser username: Personson
     * @param username
     * @return
     */
    @Override
    public User getUserByUsername(String username) {
        if (onlyUser.getUsername().equals(username)){
            return onlyUser;
        }
        return null;
    }

    @Override
    public User getUserPreferences(User user) {
        return null;
    }

    /**
     * returns onlySession, id: testSessionId
     * @param sessionId
     * @return
     */
    @Override
    public Session getSessionById(String sessionId) {
        Session result = null;
        if (sessionId.equals(onlySession.getSessionId())){
            result = onlySession;
        }
        return result;
    }

    @Override
    public byte[] enHash(String text) throws NoSuchAlgorithmException {
        return new byte[0];
    }

    @Override
    public boolean editPreferences(User user) throws NoSuchAlgorithmException {
        return false;
    }

    @Override
    public boolean removePreferences(User user) {
        return false;
    }

    @Override
    public boolean deleteUser(User user) {
        return false;
    }
}
