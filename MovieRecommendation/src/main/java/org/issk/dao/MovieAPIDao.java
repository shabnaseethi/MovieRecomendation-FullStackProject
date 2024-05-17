package org.issk.dao;

import org.issk.dto.Movie;

import java.util.ArrayList;

public interface MovieAPIDao {
    public ArrayList<Movie> fetchMovies();
}
