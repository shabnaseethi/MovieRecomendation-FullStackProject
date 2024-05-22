package org.issk.dao;


import org.issk.dto.Genre;
import org.issk.dto.Movie;
import org.issk.dto.Session;
import org.issk.dto.User;
import org.issk.exceptions.InvalidInputException;
import org.issk.mappers.GenreMapper;
import org.issk.mappers.SessionMapper;
import org.issk.mappers.UserMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;

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
     * @throws NoSuchAlgorithmException
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

    /**
     * Store a session object in the database
     * @param session session to store
     * @return true if storing successful, false otherwise.
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean storeSession(Session session) throws NoSuchAlgorithmException {
        //Check if session is in DB
        if (getSessionById(session.getSessionId()) != null){
            return false;
        }

        //Insert session into database
        int rowsAltered = jdbcTemplate.update("INSERT INTO sessions (sessionId, userId, sessionStart, sessionPeriod) VALUES (?, ?, ?, ?);",
                session.getSessionId(),
                session.getUser().getUserId(),
                session.getStartTime(),
                session.getPeriodHours());

        //Check to see if database was altered, return true if so, return false if not
        return rowsAltered>0;
    }

    /**
     * Removes a given session from the database, effectively ending the session
     * @param session session to be removed
     * @return true if a session was removed, false otherwise
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean removeSession(Session session){
        //Check to make sure session is in DB
        if (getSessionById(session.getSessionId()) == null){
            return false;
        }

        //Insert session into database
        int rowsAltered = jdbcTemplate.update("DELETE FROM sessions WHERE sessionId = ?;", session.getSessionId());

        //Check to see if database was altered, return true if so, return false if not
        return rowsAltered>0;
    }

    /**
     * Checks to see if the given session is a valid session, within the expiry time
     * @param session session to be validated
     * @return true if the session is still valid, false otherwise
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean checkSessionValid(Session session) throws NoSuchAlgorithmException{
        //Get session in database with matching ID
        Session storedSession = getSessionById(session.getSessionId());
        if (storedSession == null){
            return false;
        }
        //Check if it's expired
        if (storedSession
                .getStartTime()
                .plusHours(storedSession.getPeriodHours())
                .isBefore(LocalDateTime.now())) {
            return false;
        }
        //Otherwise, return true;
        return true;
    }

    /**
     * Checks to see if hash of password matches the one in the database
     * @param user user to be authenticated
     * @return true if the user is valid, false otherwise
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean authenticateUser(User user) throws NoSuchAlgorithmException {
        //Hash password
        byte[] passwordHash = enHash(user.getPassword());

        //Find user in database
        try {
            User dbUser = jdbcTemplate.queryForObject("SELECT * FROM users WHERE userName = ? AND passwordHash = ?;", new UserMapper(), user.getUsername(), passwordHash);
        } catch (DataAccessException e){
            return false;
        }
        return true;
    }

    public User getUserByID(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE uid = ?;", new UserMapper(), id);
        } catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE userName = ?;", new UserMapper(), username);
        } catch (DataAccessException e){
            return null;
        }
    }

    /**
     * Sets the given User object's preferences list to the list in the database
     * Should only be used to populate an empty user's preferences list.
     * Database remains unchanged
     * @param user user whose preferences list is to be updated FROM the database
     * @return the same user object, with its preferences updated from the database
     */
    @Override
    public User getUserPreferences(User user){
        try {
            List<Genre> prefGenres = jdbcTemplate.query("SELECT * FROM genre_preferences " +
                    "LEFT JOIN genres ON genres.genreId = genre_preferences.genreId " +
                    "WHERE userId = ?;", new GenreMapper(), user.getUserId());
            HashMap<Integer, Genre> prefGenresHash = new HashMap<Integer, Genre>();
            for (Genre genre : prefGenres){
                prefGenresHash.put(genre.getGenreId(), genre);
            }
            user.setPreferredGenres(prefGenresHash);
            return user;
        } catch (DataAccessException e){
            return null;
        }
    }


    @Override
    public Session getSessionById(String sessionId) {
        try{
            return jdbcTemplate.queryForObject( "SELECT sessions.*, users.* " +
                    "FROM sessions " +
                    "LEFT JOIN users ON sessions.userId = users.uid " +
                    "WHERE sessions.sessionId = ?", new SessionMapper(), sessionId);
        } catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public byte[] enHash(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(text.getBytes());
    }

    /**
     * Edits the user's genre preferences by adding new preferences.
     *
     * This method retrieves a list of genre IDs based on the genre names provided in the user's preferred genres.
     * It then checks if each genre preference already exists for the user in the database. If a preference does not
     * exist, it inserts a new preference into the database.
     *
     * @param user the user object containing the updated genre preferences and userID
     * @return true if any new preferences were added successfully, false otherwise
     * @throws DataAccessException if an error occurs while accessing the data source
     */
    @Override
    public boolean editPreferences(User user) throws DataAccessException, InvalidInputException {

//        get a list of genre IDS based on genre names
        List<Integer> genreIds = findGenreIds(user.getPreferredGenres().values().stream()
                .map(Genre::getName) // Extract the genreName from each Genr
                .collect(Collectors.toList()));

        String query = "SELECT COUNT(*) FROM genre_preferences WHERE userId = ? AND genreId = ?";
        int rowsAffected = 0; //To keep track the number of rows affected by the query

        for (Integer genreId : genreIds) {
            Integer dbCount = jdbcTemplate.queryForObject(query, Integer.class, user.getUserId(), genreId);
            int count = (dbCount != null) ? dbCount : 0;

            if (count == 0) {
                rowsAffected += count;
                int rowsAltered = jdbcTemplate.update(
                        "INSERT INTO genre_preferences (userId, genreId) VALUES (?, ?);",
                        user.getUserId(),
                        genreId
                );
                rowsAffected += rowsAltered;
            }
        }

        return rowsAffected > 0;
    }

    /**
     * Removes the user's genre preferences based on the provided user object.
     *
     * This method retrieves a list of genre IDs based on the genre names provided in the user's preferred genres.
     * It then deletes the corresponding genre preferences from the database for the specified user and each genre ID.
     *
     * @param user the user object containing the genre preferences to remove and userID
     * @return true if any preferences were successfully removed, false otherwise
     * @throws DataAccessException if an error occurs while accessing the data source
     */
    @Override
    public boolean removePreferences(User user) throws DataAccessException, InvalidInputException {

        // Get a list of genre IDs based on genre names
        List<Integer> genreIds = findGenreIds(user.getPreferredGenres().values().stream()
                .map(Genre::getName) // Extract the genreName from each Genre object
                .collect(Collectors.toList()));
        String query = "DELETE FROM genre_preferences WHERE genreId = ? AND userId = ?";

        int rowsAffected = 0;//To keep the track of rows affected by the delete query
        for (Integer genreId : genreIds) {
//            Return the number of rows affected by the query
            int count = jdbcTemplate.update(query,genreId, user.getUserId());
            rowsAffected += count;
            if (count > 0) {
                jdbcTemplate.update(query, genreId, user.getUserId());

            }
        }
        return rowsAffected > 0;
    }


    /**
     * Deletes a user from the system along with associated data.
     *
     * This method first deletes sessions associated with the user from the sessions table.
     * It then checks if the user exists in the users table and if the user has associated genre preferences
     * or favorite movies in the respective tables. If the user exists, it deletes the user and associated data.
     *
     * @param user the user object to be deleted
     * @return true if the user and associated data were successfully deleted, false otherwise
     */
    @Transactional
    @Override
    public boolean deleteUser(User user) {

        // Delete sessions associated with the user
        jdbcTemplate.update("DELETE FROM sessions WHERE userId = ?", user.getUserId());

        // Check if the user exists and if they have preferences or favourite movies
        String query = "SELECT " +
                "u.uid AS userExists, " +
                "gp.userId AS userHasPreferences, " +
                "fm.userId AS userHasFavourites " +
                "FROM users u " +
                "LEFT JOIN genre_preferences gp ON u.uid = gp.userId " +
                "LEFT JOIN favourite_movies fm ON u.uid = fm.userId " +
                "WHERE u.uid = ?";

//        List of maps with keys as column aliases and value as the number and each map represents a row in the result set.
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query, user.getUserId());

        if (results.isEmpty()) {
            return false;
        }

//        To confirm the existence of users,movies and genres.
        boolean userExists = results.stream().anyMatch(row -> row.get("userExists") != null);
        boolean userHasPreferences = results.stream().anyMatch(row -> row.get("userHasPreferences") != null);
        boolean userHasFavourites = results.stream().anyMatch(row -> row.get("userHasFavourites") != null);

        if (!userExists) {
            return false;
        }

        // Delete associated records if they exist
        if (userHasPreferences) {
            jdbcTemplate.update("DELETE FROM genre_preferences WHERE userId = ?", user.getUserId());
        }

        if (userHasFavourites) {
            jdbcTemplate.update("DELETE FROM favourite_movies WHERE userId = ?", user.getUserId());
        }
        // Delete user from users table
        jdbcTemplate.update("DELETE FROM users WHERE uid = ?", user.getUserId());

        return true;
    }

    @Override
    public boolean addFavouriteMovies(User user) {

        List<Integer> movieIds = user.getFavouriteMovies().values().stream()
                .map(Movie::getId)
                .collect(Collectors.toList());

        String query = "SELECT COUNT(*) FROM favourite_movies WHERE userId = ? AND movieId = ?";
        int rowsAffected = 0;

        for (Integer movieId : movieIds) {
            Integer dbCount = jdbcTemplate.queryForObject(query, Integer.class, user.getUserId(), movieId);
            int count = (dbCount != null) ? dbCount : 0;

            if (count == 0) {
                rowsAffected += count;

                int rowsAltered = jdbcTemplate.update(
                        "INSERT INTO favourite_movies (userId, movieId) VALUES (?, ?);",
                        user.getUserId(),
                        movieId
                );
                rowsAffected += rowsAltered;
            }
        }

        return rowsAffected > 0;
    }

    public int findGenreIdByName(String genreName) throws InvalidInputException {
        String sql = "SELECT genreId FROM genres WHERE genreName = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{genreName}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            throw new InvalidInputException("Genre not found: " + genreName);
        }
    }

    private List<Integer> findGenreIds(List<String> genreNames) throws InvalidInputException {
        List<Integer> genreIds = new ArrayList<>();
        for (String genreName : genreNames) {
            int genreId = findGenreIdByName(genreName);
            if (genreId != -1) {
                genreIds.add(genreId);
            }
        }
        return genreIds;
    }

}
