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
    void checkSessionValid() {
    }

    @Test
    void authenticateUser() {
    }

    @Test
    void getUserByUsername() {
    }
}