package org.issk.dao;

import org.issk.dto.Movie;

import java.util.ArrayList;
import java.util.List;

public interface MovieAPIDao {

    public List<Movie> getMoviesByGenre(String genre);
    public int getGenreIdFromGenreName(String genre);
    public List<Movie> getMoviesByRating(String ratingfrom,String ratingto, Boolean sort);
    public List<Movie> getMoviesByPreferences(String userid);
}
