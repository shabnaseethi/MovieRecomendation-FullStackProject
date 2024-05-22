package org.issk.service;

import org.issk.dao.MovieAPIDao;
import org.issk.dao.MovieAPIDaoAPIImpl;
import org.issk.dto.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieServiceImplTest {
    private MovieServiceImpl movieServiceImpl;

    @BeforeEach
    public void setUp() {
        movieServiceImpl = new MovieServiceImpl();
    }


    @Test

    @DisplayName("Find the movies by Genre=drama")
    void getMoviesByGenre() throws GenreNotFoundException {
        // Arrange
        String genre = "drama";

        // Act
        List<Movie> result = movieServiceImpl.getMoviesByGenre(genre);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(20, result.size());

    }

    @Test
    @DisplayName("Handle GenreNotFoundException")
    void getMoviesByGenreThrowsGenreNotFoundException() throws GenreNotFoundException {
        // Arrange
        String genre = "unknown_genre";

        // Act & Assert
        List<Movie> movies = movieServiceImpl.getMoviesByGenre(genre);
        //movieDaoDBImpl.addMoviesFromAPI(movies);
        //movieAPIDao.getMoviesByGenre(genre);
        if (movies == null || movies.isEmpty()) {
            throw new GenreNotFoundException("Genre not found: " + genre);
        }


    }

    @Test
    void getMoviesByRating() {

    }

    @Test
    @DisplayName("Find movies by user preferences")
    void testGetMoviesByPreferences() {
        String userId = "someUserId";
        List<Movie> movies = null;
        // Assuming some movies are returned for the given user ID
        try {
            movies = movieServiceImpl.getMoviesByPreferences(userId);
            assertNotNull(movies);
            assertFalse(movies.isEmpty());
        } catch (GenreNotFoundException e) {
            fail("Unexpected GenreNotFoundException: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Throw GenreNotFoundException when no movies found for user preferences")
    void testGetMoviesByPreferencesThrowsGenreNotFoundException() {
        // Arrange
        String userId = "unknownUserId";

        // Act & Assert
        GenreNotFoundException exception = assertThrows(GenreNotFoundException.class, () -> {
            movieServiceImpl.getMoviesByPreferences(userId);
        });

        // Assert
        assertNotNull(exception);
        assertEquals("Genre not found for user preferences: " + userId, exception.getMessage());

    }
}