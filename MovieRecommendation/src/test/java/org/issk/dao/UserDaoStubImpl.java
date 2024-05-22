package org.issk.dao;

import org.issk.dto.Genre;
import org.issk.dto.Movie;
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

        Movie movie = new Movie();
        movie.setId(2130);
        movie.setTitle("The Secret");
        HashMap<Integer,Movie> favMovie = new HashMap<>();
        favMovie.put(2130,movie);

        onlyUser = new User();
        onlyUser.setUserId(999);
        onlyUser.setUsername("Personson");
        onlyUser.setPassword("baklava");
        HashMap<Integer, Genre> preferredGenres = new HashMap<>();
        preferredGenres.put(999, onlyGenre);


        onlyUser.setPreferredGenres(preferredGenres);
        onlyUser.setFavouriteMovies(favMovie);

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
    public boolean removeSession(Session session) {
        if (session.getSessionId().equals(onlySession.getSessionId())){
            return true;
        }
        return false;
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
        return true;
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
        return user;
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
        Map<Integer, Genre> userPreferredGenres = user.getPreferredGenres();
        for (Integer genreId : userPreferredGenres.keySet()) {
            if (onlyUser.getPreferredGenres().containsKey(genreId)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removePreferences(User user) {
        Map<Integer, Genre> userPreferredGenres = user.getPreferredGenres();
        for (Integer genreId : userPreferredGenres.keySet()) {
            if (onlyUser.getPreferredGenres().containsKey(genreId)) {
                onlyUser.getPreferredGenres().remove(genreId);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteUser(User user) {
        if(user.getUserId() == onlyUser.getUserId()) return true;
        return  false;
    }

    @Override
    public boolean addFavouriteMovies(User user) {
        Map<Integer, Movie> userFavMovies = user.getFavouriteMovies();
        for (Integer Id : userFavMovies.keySet()) {
            if (onlyUser.getFavouriteMovies().containsKey(Id)) {
                return false;
            }
        }
        return true;
    }
}
