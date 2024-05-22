package org.issk.dao;


import org.issk.dto.Movie;
import java.util.List;
import java.util.ArrayList;

public interface MovieDao {
    public ArrayList<Movie> getMovies();
    public ArrayList<Movie> getMoviesByPreferences(String preferences);
    public ArrayList<Movie> getMoviesByGenre(String Genre);
    public void addMoviesFromAPI(List<Movie> movies);
}
