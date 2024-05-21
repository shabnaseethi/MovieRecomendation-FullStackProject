package org.issk.service;

import org.issk.dao.MovieAPIDao;
import org.issk.dao.MovieAPIDaoAPIImpl;
import org.issk.dao.MovieDaoDBImpl;
import org.issk.dto.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import  org.issk.service.GenreNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieAPIDao movieAPIDao;
    //private final MovieDaoDBImpl movieDaoDBImpl;

    @Autowired
    public MovieServiceImpl(){
        RestTemplate temp = new RestTemplate();
        this.movieAPIDao= new MovieAPIDaoAPIImpl(temp);
        //this.movieDaoDBImpl = movieDaoDBImpl;
    }

    @Override
    public List<Movie> getMoviesByGenre(String genre) throws GenreNotFoundException{
        List<Movie> movies = movieAPIDao.getMoviesByGenre(genre);
        //movieDaoDBImpl.addMoviesFromAPI(movies);
        //movieAPIDao.getMoviesByGenre(genre);
        if (movies == null || movies.isEmpty()) {
            throw new GenreNotFoundException("Genre not found: " + genre);
        }
        return movies;
    }

    @Override
    public List<Movie> getMoviesByRating(String ratingfrom,String ratingto, boolean sort)throws InvalidRatingsException {
        List<Movie> movies = movieAPIDao.getMoviesByRating(ratingfrom, ratingto, sort);
        //movieDaoDBImpl.addMoviesFromAPI(movies);
        //movieAPIDao.getMoviesByGenre(genre);

        return movies;
    }

    @Override
    public List<Movie> getMoviesByPreferences(String userid) throws GenreNotFoundException {
        List<Movie> movies = movieAPIDao.getMoviesByPreferences(userid);
        //movieDaoDBImpl.addMoviesFromAPI(movies);
        //movieAPIDao.getMoviesByGenre(genre);
        if (movies == null || movies.isEmpty()) {
            throw new GenreNotFoundException("Genre not found: ");
        }
        return movies;
    }

    @Override
    public List<Movie> getMoviesByName(String name) {
        List<Movie> movies = movieAPIDao.getMoviesByName(name);
        return movies;
    }
}
