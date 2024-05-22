package org.issk.dao;



import com.sun.net.httpserver.HttpServer;
import org.issk.dto.Movie;
import org.issk.dto.MovieResponse;
import org.issk.exceptions.GenreNotFoundException;
import org.issk.exceptions.InvalidRatingsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DataJdbcTest
class MovieAPIDaoAPIImplTest {
    private final MovieAPIDao movieAPIDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MovieAPIDaoAPIImplTest(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.movieAPIDao = new MovieAPIDaoAPIImpl(jdbcTemplate);
    }
    @Test
    void testGetMoviesByGenre() throws GenreNotFoundException {
        String genre = "Drama";

        List<Movie> movies = movieAPIDao.getMoviesByGenre(genre);

        assertNotNull(movies);
        assertEquals(20, movies.size());
    }

    @Test
    void testGetMoviesObjectIsInitialisedCorrectly() throws GenreNotFoundException {
        String genre = "Drama";

        List<Movie> movies = movieAPIDao.getMoviesByGenre(genre);

        assertNotNull(movies);
        assertTrue(movies.stream().map(Movie::getTitle).noneMatch(String::isEmpty));
        assertTrue(movies.stream().map(Movie::getVote_average).noneMatch(y->y.isNaN()));
        //assertTrue(movies.stream().map(Movie::getId).noneMatch(String::isEmpty));
    }

    @Test
    void testGetMoviesByGenreEmptyResponse() throws GenreNotFoundException {
        String genre = "abc";

        GenreNotFoundException thrown = assertThrows(GenreNotFoundException.class, () -> {
            movieAPIDao.getMoviesByGenre(genre);
        });

        assertEquals("No Genre Found", thrown.getMessage(), "Expected exception message did not match.");

    }

    @Test
    void testGetMoviesByRating() throws InvalidRatingsException {
        String ratingFrom = "7";
        String ratingTo = "9";
        boolean sort = true;

        List<Movie> moviesSortTrue = movieAPIDao.getMoviesByRating(ratingFrom, ratingTo, sort);
        List<Movie> moviesSortFalse = movieAPIDao.getMoviesByRating(ratingFrom, ratingTo, !sort);
        assertNotNull(moviesSortTrue);
        assertEquals(20, moviesSortTrue.size());
        assertEquals(20, moviesSortFalse.size());
        assertFalse(moviesSortFalse.get(0).getVote_average() == moviesSortTrue.get(0).getVote_average());
    }

    @Test
    void testGetMoviesByPreferences() throws GenreNotFoundException {
        String userId = "1";

        List<Movie> movies = movieAPIDao.getMoviesByPreferences(userId);

        assertNotNull(movies);
        assertEquals(20, movies.size());
    }

    @Test
    void testGetMoviesByPreferencesEmptyResponse() throws GenreNotFoundException {
        /*String userId = "12345";

        List<Movie> movies = movieAPIDao.getMoviesByPreferences(userId);

        assertNotNull(movies);
        assertTrue(movies.isEmpty()); */
        String userId = "67890"; // Ensure this is a user ID not present in the database

        GenreNotFoundException thrown = assertThrows(GenreNotFoundException.class, () -> {
            movieAPIDao.getMoviesByPreferences(userId);
        });

        assertEquals("Wrong Genre", thrown.getMessage(), "Expected exception message did not match.");

    }
}


