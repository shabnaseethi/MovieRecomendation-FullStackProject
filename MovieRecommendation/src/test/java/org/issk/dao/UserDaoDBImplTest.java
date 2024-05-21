package org.issk.dao;

import org.issk.dto.Session;
import org.issk.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

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
    void getSessionByValidId() {
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
    void createUserInvalid() {
        User testUser = new User();
        testUser.setUsername("Ephemeral John");
        testUser.setPassword("test");

        User newTestUser = new User();
        newTestUser.setUsername("Ephemeral John");
        newTestUser.setPassword("test");

        boolean created = false;
        boolean newCreated = false;
        try { created = userDao.createUser(testUser); } catch (NoSuchAlgorithmException e) {}
        try { newCreated = userDao.createUser(newTestUser); } catch (NoSuchAlgorithmException e) {}

        assertTrue(created, "User should be created");
        assertFalse(newCreated, "New user with same name should not be added");
    }

    @Test
    void storeValidSession() {
        //this user exists in the database, id: 3
        User testUser = new User();
        testUser.setUserId(3);
        testUser.setUsername("person2");

        Session testSession = new Session();
        testSession.setSessionId("testSessionId");
        testSession.setUser(testUser);
        testSession.setStartTime(LocalDateTime.MIN);
        testSession.setPeriodHours(1);

        //Store session
        try { userDao.storeSession(testSession); } catch (NoSuchAlgorithmException e) {}

        //Fetch session
        Session outSession = userDao.getSessionById("testSessionId");

        //Assert
        assertNotNull(outSession, "Session should be found");
        assertEquals(outSession.getSessionId(), testSession.getSessionId(), "Session ID should match");

    }

    @Test
    void storeInvalidSession() {
        //This user exists in the database, id: 3
        User testUser = new User();
        testUser.setUserId(3);
        testUser.setUsername("person2");

        Session testSession = new Session();
        testSession.setSessionId("testSessionId");
        testSession.setUser(testUser);
        testSession.setStartTime(LocalDateTime.MIN);
        testSession.setPeriodHours(1);

        boolean duplicateStored = false;

        //Store session
        try { userDao.storeSession(testSession); } catch (NoSuchAlgorithmException e) {}
        try { duplicateStored = userDao.storeSession(testSession); } catch (NoSuchAlgorithmException e) {}

        //Fetch session
        Session outSession = userDao.getSessionById("testSessionId");

        //Assert
        assertNotNull(outSession, "Session should be found");
        assertEquals(outSession.getSessionId(), testSession.getSessionId(), "Session ID should match");

        assertFalse(duplicateStored, "The duplicate session shouldn't be stored");

    }

    @Test
    void checkSessionValid_ValidSession() {
        //This user exists in the database, id: 3
        User testUser = new User();
        testUser.setUserId(3);
        testUser.setUsername("person2");

        //Base session gets stored in DB
        Session baseTestSession = new Session();
        baseTestSession.setSessionId("testSessionId");
        baseTestSession.setUser(testUser);
        baseTestSession.setStartTime(LocalDateTime.now());
        baseTestSession.setPeriodHours(1);

        //Test session has matching ID to base (stored) session
        Session testSession = new Session();
        testSession.setSessionId("testSessionId");
        baseTestSession.setStartTime(LocalDateTime.now());
        testSession.setPeriodHours(1);

        //Store base session
        try { userDao.storeSession(baseTestSession); } catch (NoSuchAlgorithmException e) {}

        //Assert session matches in DB
        boolean valid = false;
        try{ valid = userDao.checkSessionValid(testSession); } catch (NoSuchAlgorithmException e) {}

        assertTrue(valid, "Session exists in database, so it should match");
    }

    @Test
    void checkSessionValid_InvalidSession() {
        //This user exists in the database, id: 3
        User testUser = new User();
        testUser.setUserId(3);
        testUser.setUsername("person2");

        //Base session gets stored in DB
        Session baseTestSession = new Session();
        baseTestSession.setSessionId("testSessionId");
        baseTestSession.setUser(testUser);
        baseTestSession.setStartTime(LocalDateTime.now());
        baseTestSession.setPeriodHours(1);

        //Test session has mismatched ID to base (stored) session
        Session testSession = new Session();
        testSession.setSessionId("differentId");
        baseTestSession.setStartTime(LocalDateTime.now());
        testSession.setPeriodHours(1);

        //Store base session
        try { userDao.storeSession(baseTestSession); } catch (NoSuchAlgorithmException e) {}

        //Assert session matches in DB
        boolean valid = false;
        try{ valid = userDao.checkSessionValid(testSession); } catch (NoSuchAlgorithmException e) {}

        assertFalse(valid, "Session ID exists not in the database, so it shan't match");
    }

    @Test
    void authenticateUserValid() {
        //This entry exists in the database, person2:password
        User testUser = new User();
        testUser.setUsername("person2");
        testUser.setPassword("password");

        boolean authentic = false;
        try { authentic = userDao.authenticateUser(testUser); } catch (NoSuchAlgorithmException e) {}

        assertTrue(authentic, "User exists in DB with correct username and password, so it should be authentic");
    }

    @Test
    void authenticateUserInvalid() {
        //This entry exists in the database, person2:password
        User testUser = new User();
        testUser.setUsername("person2");
        testUser.setPassword("wrong_password");

        boolean authentic = true;
        try { authentic = userDao.authenticateUser(testUser); } catch (NoSuchAlgorithmException e) {}

        assertFalse(authentic, "User exists in DB with different username and password, so it can't be authentic");
    }

    @Test
    void getUserByValidUsername() {
        User expectedUser = new User();
        expectedUser.setUserId(3);
        expectedUser.setUsername("person2");

        User outUser = userDao.getUserByUsername("person2");

        assertEquals(outUser.getUserId(), expectedUser.getUserId(), "Ids should match");
        assertEquals(outUser.getUsername(), expectedUser.getUsername(), "Usernames should match");
    }

    @Test
    void getUserByInvalidUsername() {
        User expectedUser = new User();
        expectedUser.setUserId(3);
        expectedUser.setUsername("person2");

        //hopefully no users use this username...
        User outUser = userDao.getUserByUsername("not a valid username");

        assertNull(outUser);
    }
}