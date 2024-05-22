package org.issk.service;

import org.issk.dao.MovieAPIDao;
import org.issk.dao.MovieAPIDaoAPIImpl;
import org.issk.dto.Movie;
import org.issk.exceptions.InvalidRatingsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.issk.exceptions.GenreNotFoundException;

import org.springframework.web.client.RestTemplate;


import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieAPIDao movieAPIDao;
    //private final MovieDaoDBImpl movieDaoDBImpl;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MovieServiceImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        //this.movieService = new MovieServiceImpl(jdbcTemplate);
        this.movieAPIDao = new MovieAPIDaoAPIImpl(jdbcTemplate);
    }

    @Override
    public List<Movie> getMoviesByGenre(String genre) throws GenreNotFoundException{
        // Validate genre parameter
        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty");
        }

        // Fetch movies by genre
        List<Movie> movies = movieAPIDao.getMoviesByGenre(genre);
        if (movies == null || movies.isEmpty()) {
            throw new GenreNotFoundException("No Genre Found");
        }

        return movies;
    }

    @Override
    public List<Movie> getMoviesByRating(String ratingfrom,String ratingto, boolean sort)throws InvalidRatingsException {
      /*  List<Movie> movies = movieAPIDao.getMoviesByRating(ratingfrom, ratingto, sort);
        //movieDaoDBImpl.addMoviesFromAPI(movies);
        //movieAPIDao.getMoviesByGenre(genre);

        return movies; */

        // Validate ratingFrom parameter
        if (ratingfrom == null || ratingfrom.isEmpty()) {
            throw new IllegalArgumentException("Rating from cannot be null or empty");
        }
        // Validate ratingTo parameter
        if (ratingto == null || ratingto.isEmpty()) {
            throw new IllegalArgumentException("Rating to cannot be null or empty");
        }
        // Validate sort parameter
        if (sort != true && sort != false) {
            throw new IllegalArgumentException("Invalid value for sort parameter. Expected true or false.");
        }
        // Fetch movies by rating
        List<Movie> movies = movieAPIDao.getMoviesByRating(ratingfrom, ratingto, sort);
        return movies;
    }

    @Override
    public List<Movie> getMoviesByPreferences(String userid) throws GenreNotFoundException {
        // Validate userId parameter
        if (userid == null || userid.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        // Fetch movies by user preferences
        List<Movie> movies = movieAPIDao.getMoviesByPreferences(userid);
        if (movies == null || movies.isEmpty()) {
            throw new GenreNotFoundException("Genre not found for user preferences: " + userid);
        }
        return movies;
    }

    @Override
    public List<Movie> getMoviesByName(String name) {
        List<Movie> movies = movieAPIDao.getMoviesByName(name);
        return movies;
    }
}
