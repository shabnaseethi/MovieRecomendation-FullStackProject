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
}