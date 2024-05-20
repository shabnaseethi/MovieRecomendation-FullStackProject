package org.issk.service;

import org.issk.dto.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> getMoviesByGenre(String genre);
    List<Movie> getMoviesByRating(String rating, boolean sort);
    List<Movie> getMoviesByPreferences(String userid);

}
