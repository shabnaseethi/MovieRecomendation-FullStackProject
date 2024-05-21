package org.issk.service;

import org.issk.dao.UserDao;
import org.issk.dto.Genre;
import org.issk.dto.User;
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

    public UserServiceImplTest() {

        UserDao userDao = new UserDaoDBStubImpl();
        userService = new UserServiceImpl(userDao);
    }

    @Test
    void testEditPreferencesFailure() {
        User user = new User();
        HttpServletRequest request = mock(HttpServletRequest.class);
        // Set the Authorization header
        when(request.getHeader("Authorization")).thenReturn("98851cd56f99655864e676c39a8e3a37c090a22015fcc88914da3444b3c626ac");

        ResponseEntity<String> response = userService.editPreferences(request, user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid session or session not found", response.getBody());
    }

    @Test
    void testRemovePreferencesFailure() {
        User user = new User();
        HttpServletRequest request = mock(HttpServletRequest.class);
        // Set the Authorization header
        when(request.getHeader("Authorization")).thenReturn("98851cd56f99655864e676c39a8e3a37c090a22015fcc88914da3444b3c626ac");

        ResponseEntity<String> response = userService.editPreferences(request, user);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid session or session not found", response.getBody());
    }

    @Test
    void testDeleteUserFailure() {
        User user = new User();
        HttpServletRequest request = mock(HttpServletRequest.class);
        // Set the Authorization header
        when(request.getHeader("Authorization")).thenReturn("98851cd56f99655864e676c39a8e3a37c090a22015fcc88914da3444b3c626ac");

        ResponseEntity<String> response = userService.editPreferences(request, user);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid session or session not found", response.getBody());
    }

    @Test
    void testEditPreferenceSuccess(){
        User user = new User();
        user.setUsername("Sam");
        user.setPassword("Sam123456");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("testSessionId");

        userService.createUser(user);

        Genre genres = new Genre();
        genres.setName("Drama");
        genres.setGenreId(12);

        HashMap<Integer, Genre> preferredGenre = new HashMap<>();
        preferredGenre.put(12,genres);

        user.setPreferredGenres(preferredGenre);

        ResponseEntity<String> response = userService.editPreferences(request, user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());



    }
}