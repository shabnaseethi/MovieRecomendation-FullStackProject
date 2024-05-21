package org.issk.service;

import org.issk.dao.UserDao;
import org.issk.dao.UserDaoStubImpl;
import org.issk.dto.Genre;
import org.issk.dto.Session;
import org.issk.dto.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserDaoStubImpl userDao;

    public UserServiceImplTest() {

        userDao = new UserDaoStubImpl();
        userService = new UserServiceImpl(userDao);
    }

    @Test
    void createUserValid() {
        User testUser = new User();
        testUser.setUsername("test");
        testUser.setPassword("validpassword");

        //Run
        int output = userService.createUser(testUser);

        //Assert
        assertEquals(output, 204, "User should be successfully created");
        assertEquals(userDao.onlyUser.getUsername(), testUser.getUsername(), "Usernames should match");
    }

    @Test
    void createUserInvalidPassword() {
        User testUser = new User();
        testUser.setUsername("test");
        testUser.setPassword("no");

        //Run
        int output = userService.createUser(testUser);

        //Assert
        assertEquals(output, 400, "User should not be created");
        assertNotEquals(userDao.onlyUser.getUsername(), testUser.getUsername(), "Usernames should not be sent to dao");
    }

    @Test
    void loginValid() {
        User testUser = new User();
        testUser.setUsername("Personson");
        testUser.setPassword("baklava");

        Session session = userService.login(testUser);

        assertNotNull(session, "A new session should be returned");
        assertEquals(testUser.getUsername(), session.getUser().getUsername(), "Usernames should match");
    }

    @Test
    void loginInvalid() {
        User testUser = new User();
        testUser.setUsername("Personson");
        testUser.setPassword("wrongpassword");

        Session session = userService.login(testUser);

        assertNull(session, "No session should be returned");
    }

    @Test
    @DisplayName("EditPreferenceFailInvalidSession")
    void testEditPreferencesFailure() {
        User user = new User();
        HttpServletRequest request = mock(HttpServletRequest.class);
        // Set the Authorization header
        when(request.getHeader("Authorization")).thenReturn("testSessionID");

        ResponseEntity<String> response = userService.editPreferences(request, user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid session or session not found", response.getBody());
    }

    @Test
    @DisplayName("RemovePreferenceFailInvalidSession")
    void testRemovePreferencesFailure() {
        User user = new User();
        HttpServletRequest request = mock(HttpServletRequest.class);
        // Set the Authorization header
        when(request.getHeader("Authorization")).thenReturn("testSessionID");

        ResponseEntity<String> response = userService.editPreferences(request, user);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid session or session not found", response.getBody());
    }

    @Test
    @DisplayName("DeleteUserFailInvalidSession")
    void testDeleteUserFailure() {
        User user = new User();
        HttpServletRequest request = mock(HttpServletRequest.class);
        // Set the Authorization header
        when(request.getHeader("Authorization")).thenReturn("testSessionID");

        ResponseEntity<String> response = userService.editPreferences(request, user);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid session or session not found", response.getBody());
    }

    @Test
    @DisplayName("EditPreferenceSuccess")
    void testEditPreferenceSuccess(){
        User user = new User();
        user.setUsername("Personson");
        user.setPassword("baklava");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer testSessionId");

        Genre genres = new Genre();
        genres.setName("Drama");
        genres.setGenreId(12);

        HashMap<Integer, Genre> preferredGenre = new HashMap<>();
        preferredGenre.put(12,genres);

        user.setPreferredGenres(preferredGenre);

        ResponseEntity<String> response = userService.editPreferences(request, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Preferences edited successfully",response.getBody());


    }

    @Test
    @DisplayName("RemovePreferencSuccess")
    void testRemovePreferenceSuccess(){
        User user = new User();
        user.setUsername("Personson");
        user.setPassword("baklava");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer testSessionId");

        Genre genres = new Genre();
        genres.setName("Tachyonesque");
        genres.setGenreId(999);

        HashMap<Integer, Genre> preferredGenre = new HashMap<>();
        preferredGenre.put(999,genres);

        user.setPreferredGenres(preferredGenre);

        ResponseEntity<String> response = userService.removePreferences(request, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Preferences removed successfully",response.getBody());
    }

    @Test
    @DisplayName("DeleteUserSuccess")
    void testDeleteUserSuccess(){
        User user = new User();
        user.setUsername("Personson");
        user.setPassword("baklava");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer testSessionId");

        Genre genres = new Genre();
        genres.setName("Drama");
        genres.setGenreId(12);

        HashMap<Integer, Genre> preferredGenre = new HashMap<>();
        preferredGenre.put(12,genres);

        user.setPreferredGenres(preferredGenre);

        ResponseEntity<String> response = userService.deleteUser(request, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User removed successfully",response.getBody());


    }

    @Test
    @DisplayName("EditPreferenceGenreNotRemoved")
    void testEditPreferencesWithGenreNotExisting() {
        User user = new User();
        user.setUsername("Personson");
        user.setPassword("baklava");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer testSessionId");

        Genre genres = new Genre();
        genres.setName("Tachyonesque");
        genres.setGenreId(999);

        HashMap<Integer, Genre> preferredGenre = new HashMap<>();
        preferredGenre.put(999,genres);

        user.setPreferredGenres(preferredGenre);

        ResponseEntity<String> response = userService.editPreferences(request, user);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to edit preferences:Preference already exists",response.getBody());
    }

    @Test
    @DisplayName("RemovePreferenceNotExisting")
    void testRemovePreferencesNotExisting() {
        User user = new User();
        user.setUsername("Personson");
        user.setPassword("baklava");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer testSessionId");

        Genre genres = new Genre();
        genres.setName("Drama");
        genres.setGenreId(9);

        HashMap<Integer, Genre> preferredGenre = new HashMap<>();
        preferredGenre.put(9,genres);

        user.setPreferredGenres(preferredGenre);

        ResponseEntity<String> response = userService.removePreferences(request, user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to remove preferences/No preferences to remove",response.getBody());
    }
}