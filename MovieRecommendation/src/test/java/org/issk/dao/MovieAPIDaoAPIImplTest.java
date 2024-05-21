package org.issk.dao;



import org.issk.dto.Movie;
import org.issk.dto.MovieResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class MovieAPIDaoAPIImplTest {

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Find the movie by genre 'Drama' ")
    void getMoviesByGenre() {
       String genre ="drama";
       MovieAPIDaoAPIImpl movieAPIDao1 = new MovieAPIDaoAPIImpl(new RestTemplate());
       // Call the method
       List<Movie> result = movieAPIDao1.getMoviesByGenre(genre);

        // Verify the results
        assertNotNull(result);
        assertEquals(20, result.size());
        // Verify the interaction with restTemplate


    }

    @Test
    @DisplayName("Find the movies by GenreId= 878")
    void getGenreIdFromGenreName() {

        //int genreId=878;
        String genre ="drama";
        MovieAPIDaoAPIImpl movieAPIDaoAPI=new MovieAPIDaoAPIImpl(new RestTemplate());

        int result= movieAPIDaoAPI.getGenreIdFromGenreName(genre);
        assertNotNull(result);
        assertEquals(878,result);

    }

    @Test
    @DisplayName("Get User PreferenceGenreIDs")
    void getUserPreferenceGenreIds() {

        String id="878|28";
        MovieAPIDaoAPIImpl movieAPIDaoAPI=new MovieAPIDaoAPIImpl(new RestTemplate());

        String result= movieAPIDaoAPI.getUserPreferenceGenreIds(id);
        assertNotNull(result);
        assertEquals("878|28",result);
    }

    @Test
    @DisplayName("Get movies by rating  between 8 to 9")
    void getMoviesByRating() {
        boolean sort=true;
        String sortOrder = "vote_average." + (sort ? "asc" : "desc");
        MovieAPIDaoAPIImpl movieAPIDaoAPI=new MovieAPIDaoAPIImpl(new RestTemplate());
       // int result=movieAPIDaoAPI.getMoviesByRating(Integer.parseInt("8"),sort);
      //  assertNotNull(result);
       // assertEquals(sortOrder,result);


    }

    @Test
    void getMoviesByPreferences() {
    }
}