package org.issk.service;

import org.issk.dao.MovieAPIDao;
import org.issk.dao.MovieAPIDaoAPIImpl;
import org.issk.dao.UserDao;
import org.issk.dao.UserDaoStubImpl;
import org.issk.dto.Movie;
import org.issk.exceptions.GenreNotFoundException;
import org.issk.exceptions.InvalidRatingsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJdbcTest
class MovieServiceImplTest {
    private final MovieService movieService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MovieServiceImplTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.movieService = new MovieServiceImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("Find the movies by Genre=drama")
    void getMoviesByGenre() throws GenreNotFoundException {
        // Arrange
        String genre = "drama";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            movieService.getMoviesByGenre(genre);
        });

        assertEquals("Genre cannot be null or empty", thrown.getMessage(), "Expected exception message did not match.");


    }

    @Test
    @DisplayName("Handle GenreNotFoundException")
    void getMoviesByGenreThrowsGenreNotFoundException() throws GenreNotFoundException {
        // Arrange
        String genre = "unknown_genre";

        // Act & Assert
        List<Movie> movies = movieService.getMoviesByGenre(genre);
        //movieDaoDBImpl.addMoviesFromAPI(movies);
        //movieAPIDao.getMoviesByGenre(genre);
        if (movies == null || movies.isEmpty()) {
            throw new GenreNotFoundException("No Genre Found ");
        }


    }


    @Test
    @DisplayName("Find movies by user preferences")
    void testGetMoviesByPreferences() throws GenreNotFoundException {
        String userId = "1";
        List<Movie> movies = null;
        // Assuming some movies are returned for the given user ID
        try {
            movies = movieService.getMoviesByPreferences(userId);
            assertNotNull(movies);
            assertFalse(movies.isEmpty());
        } catch (GenreNotFoundException e) {
            fail("Unexpected GenreNotFoundException: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Throw GenreNotFoundException when no movies found for user preferences")
    void testGetMoviesByPreferencesThrowsGenreNotFoundException() throws GenreNotFoundException {
        // Arrange
        String userId = "unknownUserId";

        // Act & Assert
        GenreNotFoundException exception = assertThrows(GenreNotFoundException.class, () -> {
            movieService.getMoviesByPreferences(userId);
        });

        // Assert
        assertNotNull(exception);
        assertEquals("Genre not found for user preferences: " + userId, exception.getMessage());

    }

    @Test
    void getMoviesByRating_ValidInput_ReturnsMovies() throws InvalidRatingsException {
        // Arrange
        String ratingFrom = "5";
        String ratingTo = "8";
        boolean sort = true;

        // Act
        List<Movie> movies = movieService.getMoviesByRating(ratingFrom, ratingTo, sort);

        // Assert
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        // Add more assertions based on your requirements
    }

    @Test
    void getMoviesByRating_InvalidRatingFrom_ThrowsInvalidRatingsException() {
        // Arrange
        String ratingFrom = null; // Invalid ratingFrom
        String ratingTo = "8";
        boolean sort = true;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            movieService.getMoviesByRating(ratingFrom, ratingTo, sort);
        });
    }

    @Test
    void getMoviesByRating_InvalidRatingTo_ThrowsInvalidRatingsException() {
        // Arrange
        String ratingFrom = "5";
        String ratingTo = ""; // Invalid ratingTo
        boolean sort = true;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            movieService.getMoviesByRating(ratingFrom, ratingTo, sort);
        });
    }

    @Test
    void getMoviesByRating_InvalidSortParameter_ThrowsIllegalArgumentException() {
        // Arrange
        String ratingFrom = "5";
        String ratingTo = "8";
        boolean sort = true; // Invalid sort parameter

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            movieService.getMoviesByRating(ratingFrom, ratingTo, sort);
        });
    }

}
