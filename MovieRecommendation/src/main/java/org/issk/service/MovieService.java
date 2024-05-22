package org.issk.service;

import org.issk.dto.Movie;
import org.issk.exceptions.GenreNotFoundException;
import org.issk.exceptions.InvalidRatingsException;

import java.util.List;

public interface MovieService {

    List<Movie> getMoviesByGenre(String genre)throws GenreNotFoundException;
    List<Movie> getMoviesByRating(String ratingfrom,String ratingto, boolean sort)throws InvalidRatingsException;
    List<Movie> getMoviesByPreferences(String userid) throws GenreNotFoundException;

    List<Movie> getMoviesByName(String name);
}
