package org.issk.dao;


import org.issk.dto.Genre;
import org.issk.dto.Session;
import org.issk.dto.User;
import org.issk.mappers.SessionMapper;
import org.issk.mappers.UserMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public boolean editPreferences(User user) throws DataAccessException {

//        get a list of genre IDS based on genre names
        List<Integer> genreIds = findGenreIds(user.getPreferredGenres().values().stream()
                .map(Genre::getName) // Extract the genreName from each Genre object
                .collect(Collectors.toList()));

        String query = "SELECT COUNT(*) FROM genre_preferences WHERE userId = ? AND genreId = ?";
        int rowsAffected = 0; //To keep track the number of rows affected by the query

        for (Integer genreId : genreIds) {
            Integer dbCount = jdbcTemplate.queryForObject(query, Integer.class, user.getUserId(), genreId);
            int count = (dbCount != null) ? dbCount : 0;
            rowsAffected += count;
            if (count == 0) {
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
    public boolean removePreferences(User user) throws DataAccessException {

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
     * in the genre_preferences table. If the user exists in both tables, it deletes data from both tables.
     * If the user only exists in the users table, it deletes the user from that table.
     *
     * @param user the user object to be deleted
     * @return true if the user and associated data were successfully deleted, false otherwise
     */
    @Transactional
    @Override
    public boolean deleteUser(User user) {

//        delete Sessions associated with the error

      jdbcTemplate.update("DELETE FROM sessions WHERE userId = ?", user.getUserId());

        // check if the user exists in the users table
        Integer userExistsCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE uid = ?", Integer.class, user.getUserId());
        boolean userExists = userExistsCount != null && userExistsCount > 0;

        // Check if the user exists in the genre_preferences table
        Integer userHasPreferencesCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM genre_preferences WHERE userId = ?", Integer.class, user.getUserId());
        boolean userHasPreferences = userHasPreferencesCount != null && userHasPreferencesCount > 0;

        // If the user exists in both tables, delete data from both tables
        if (userExists && userHasPreferences) {
            // Delete associated records from genre_preferences table
            jdbcTemplate.update("DELETE FROM genre_preferences WHERE userId = ?", user.getUserId());

            // Delete user from users table
            jdbcTemplate.update("DELETE FROM users WHERE uid = ?", user.getUserId());

            return true;
        } else if(userExists) {

            jdbcTemplate.update("DELETE FROM users WHERE uid = ?", user.getUserId());
            return true;
        }
        else{
            return false;
        }
    }

    public int findGenreIdByName(String genreName) {
        String sql = "SELECT genreId FROM genres WHERE genreName = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{genreName}, Integer.class);
    }

    private List<Integer> findGenreIds(List<String> genreNames) {
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
