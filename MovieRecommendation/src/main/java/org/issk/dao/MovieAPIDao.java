package org.issk.dao;

import org.issk.dto.Movie;
import org.issk.exceptions.GenreNotFoundException;
import org.issk.exceptions.InvalidRatingsException;

import java.util.ArrayList;
import java.util.List;

public interface MovieAPIDao {

    public List<Movie> getMoviesByGenre(String genre)throws GenreNotFoundException;
    public int getGenreIdFromGenreName(String genre)throws  GenreNotFoundException;
    public List<Movie> getMoviesByRating(String ratingfrom,String ratingto, Boolean sort)throws InvalidRatingsException;
    public List<Movie> getMoviesByPreferences(String userid)throws GenreNotFoundException;
    public List<Movie> getMoviesByName(String name);
}
