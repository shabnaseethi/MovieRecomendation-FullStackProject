package org.issk.dao;

import org.issk.dto.Genre;
import org.issk.dto.Movie;
import org.issk.dto.Session;
import org.issk.dto.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
class UserDaoDBImplTest {

    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    @Autowired
    public UserDaoDBImplTest(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        userDao = new UserDaoDBImpl(jdbcTemplate);
    }

    @Test
    void getSessionById() {
        Session session = userDao.getSessionById("testId");
        assertNotNull(session);
        assertEquals("testId", session.getSessionId());
    }

    @Test
    void getSessionByInvalidId() {
        Session session = userDao.getSessionById("invalidTestId");
        assertNull(session);
        //assertEquals("testId", session.getSessionId());
    }

    @Test
    void createUserValid() {
        User testUser = new User();
        testUser.setUsername("Ephemeral John");
        testUser.setPassword("test");
        boolean created = false;
        try { created = userDao.createUser(testUser); } catch (NoSuchAlgorithmException e) {}
        assertTrue(created, "User should be created");
    }

    @Test
    void storeSession() {
    }

    @Test
    void checkSessionValid() {
    }

    @Test
    void authenticateUser() {
    }

    @Test
    void getUserByUsername() {
    }
    @Test
    @DisplayName("EditPreferenceSuccess")
    void editPreferencesSuccessTest() throws NoSuchAlgorithmException {
        User userTest = new User();
        userTest.setUserId(1);
        userTest.setUsername("Ann");

        Genre genresOne = new Genre();
        genresOne.setName("Action");
        genresOne.setGenreId(12);

        Genre genreTwo = new Genre();
        genreTwo.setName("Drama");
        genreTwo.setGenreId(28);

        HashMap<Integer,Genre> preferredGenre = new HashMap<>();
        preferredGenre.put(12, genresOne);
        preferredGenre.put(28,genreTwo);

        userTest.setPreferredGenres(preferredGenre);

        assertTrue(userDao.editPreferences(userTest));
    }

    @Test
    @DisplayName("RemovePreferenceSuccess")
    void removePreferencesSuccessTest() throws NoSuchAlgorithmException {
        User userTest = new User();
        userTest.setUserId(1);
        userTest.setUsername("Ann");

        Genre genres = new Genre();
        genres.setName("Action");
        genres.setGenreId(12);

        HashMap<Integer,Genre> preferredGenre = new HashMap<>();
        preferredGenre.put(1,genres);

        userTest.setPreferredGenres(preferredGenre);

        userDao.editPreferences(userTest);

        assertTrue(userDao.removePreferences(userTest));

    }

    @Test
    @DisplayName("DeleteUserSuccess")
    void deleteUserSuccessTest() throws NoSuchAlgorithmException {
        User userTest = new User();
        userTest.setUserId(1);
        userTest.setUsername("Ann");
        userTest.setPassword("123456yu");


        userDao.createUser(userTest);

        assertTrue(userDao.deleteUser(userTest));
    }

    @Test
    @DisplayName("EditPreferenceFail")
    void editPreferencesFailureTest() throws NoSuchAlgorithmException {
        User userTest = new User();
        userTest.setUserId(1);
        userTest.setUsername("Ann");

        Genre genresOne = new Genre();
        genresOne.setName("Action");
        genresOne.setGenreId(12);

        HashMap<Integer,Genre> preferredGenre = new HashMap<>();
        preferredGenre.put(12, genresOne);

        userTest.setPreferredGenres(preferredGenre);

        boolean firstEdit = userDao.editPreferences(userTest);

//        Adding same genre to the genre preferences
        Genre genresTwo = new Genre();
        genresTwo.setName("Action");
        genresTwo.setGenreId(12);

        HashMap<Integer,Genre> preferredGenreTwo = new HashMap<>();
        preferredGenre.put(12, genresTwo);

        userTest.setPreferredGenres(preferredGenreTwo);

        boolean secondEdit = userDao.editPreferences(userTest);

        assertTrue(firstEdit);
        assertFalse(secondEdit);
    }

    @Test
    @DisplayName("RemovePreferenceFail")
    void removePreferencesFailureTest() throws NoSuchAlgorithmException {
        User userTest = new User();
        userTest.setUserId(1);
        userTest.setUsername("Ann");

        Genre genres = new Genre();
        genres.setName("Drama");
        genres.setGenreId(12);

        HashMap<Integer, Genre> preferredGenre = new HashMap<>();
        preferredGenre.put(12,genres);

        userTest.setPreferredGenres(preferredGenre);

        assertFalse(userDao.removePreferences(userTest));

    }

    @Test
    @DisplayName("DeleteUserFail")
    void deleteUserFailureTest() throws NoSuchAlgorithmException {
        User userTest = new User();
        userTest.setUserId(10);
        userTest.setUsername("Ben");
        userTest.setPassword("123456yu");

        assertFalse(userDao.deleteUser(userTest));
    }

    @Test
    @DisplayName("AddFavouriteMoviesSuccess")
    void addFavouriteMoviesSuccess(){
        User userTest = new User();
        userTest.setUserId(2);
        userTest.setUsername("Phil");

        Movie movie = new Movie();
        movie.setId(2130);
        movie.setTitle("The Secret");

        HashMap<Integer,Movie> favMovie = new HashMap<>();

        favMovie.put(2130,movie);

        userTest.setFavouriteMovies(favMovie);

        assertTrue(userDao.addFavouriteMovies(userTest));


    }

}