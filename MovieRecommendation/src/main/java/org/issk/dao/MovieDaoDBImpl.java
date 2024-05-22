package org.issk.dao;

import org.issk.dto.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
@Service
public class MovieDaoDBImpl implements MovieDao {


    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public MovieDaoDBImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ArrayList<Movie> getMovies() {
        return null;
    }

    @Override
    public ArrayList<Movie> getMoviesByPreferences(String preferences) {
        return null;
    }

    @Override
    public ArrayList<Movie> getMoviesByGenre(String Genre) {
        return null;
    }

    @Override
    public void addMoviesFromAPI(List<Movie> movies) {

        }
    }

