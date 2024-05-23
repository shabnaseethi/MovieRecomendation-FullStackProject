package org.issk.dao;

import org.issk.dto.Movie;
import org.issk.dto.MovieResponse;
import org.issk.dto.User;
import org.issk.exceptions.GenreNotFoundException;
import org.issk.exceptions.InvalidRatingsException;
import org.issk.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Repository
public class MovieAPIDaoAPIImpl implements MovieAPIDao {

    private final RestTemplate restTemplate;
    private static final String API_KEY = "89afb92d2b5bba942e667df05182f34a";
    private final JdbcTemplate jdbcTemplate;


    /*

     */
    @Autowired
    public MovieAPIDaoAPIImpl(JdbcTemplate jdbcTemplate) {
        this.restTemplate = new RestTemplate();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Movie> getMoviesByGenre(String genre) throws GenreNotFoundException {
        try {
            int genreId = getGenreIdFromGenreName(genre);
            String apiUrl = "https://api.themoviedb.org/3/discover/movie?api_key=89afb92d2b5bba942e667df05182f34a";
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl).queryParam("with_genres", genreId);

            MovieResponse response = restTemplate.getForObject(uriBuilder.toUriString(), MovieResponse.class);

            if (response != null) {
                return response.getResults();
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            throw new GenreNotFoundException("No Genre Found");
        }
    }

    @Override
    public int getGenreIdFromGenreName(String genre)throws GenreNotFoundException {
        //Code Here
        String query = "select top 1 genreId from genres where genreName = ?";
        try {
            return jdbcTemplate.queryForObject(query, Integer.class, genre);
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException("No Genre Found");
        }
    }

    public String getUserPreferenceGenreIds(String userid)throws GenreNotFoundException {
        //Code Here
        String query="select genreId from genre_preferences where userId =?";

        List<Integer> genreIds = jdbcTemplate.queryForList(query, new Object[]{userid}, Integer.class);

        if(genreIds.isEmpty()){
            throw new  GenreNotFoundException("Wrong Genre");
        }
        if (!genreIds.isEmpty()) {
            return String.join("|", genreIds.stream().map(String::valueOf).toArray(String[]::new));
        } else {
            return "";
        }
    }

    @Override
    public List<Movie> getMoviesByRating(String ratingfrom, String ratingto, Boolean sort)throws InvalidRatingsException {
        String sortOrder = "vote_average." + (sort ? "asc" : "desc");
        String apiUrl="https://api.themoviedb.org/3/discover/movie?api_key=89afb92d2b5bba942e667df05182f34a";
        UriComponentsBuilder uriBuilder= UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("sort_by",sortOrder)
                .queryParam("vote_average.gte",ratingfrom)
                .queryParam("vote_average.lte",ratingto);

        MovieResponse response = restTemplate.getForObject(uriBuilder.toUriString(), MovieResponse.class);

        //Movie[] response=restTemplate.getForObject(uriBuilder.toUriString(),Movie[].class);
        if(response !=null){
            return response.getResults();
        }
        else{
            return Collections.emptyList();
        }
    }

    @Override
    public List<Movie> getMoviesByPreferences(String userid) throws GenreNotFoundException {
        String genreIds = getUserPreferenceGenreIds(userid);
        String apiUrl="https://api.themoviedb.org/3/discover/movie?api_key=89afb92d2b5bba942e667df05182f34a";
        UriComponentsBuilder uriBuilder= UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("with_genres",genreIds);

        MovieResponse response = restTemplate.getForObject(uriBuilder.toUriString(), MovieResponse.class);

        //Movie[] response=restTemplate.getForObject(uriBuilder.toUriString(),Movie[].class);
        if(response !=null){
            return response.getResults();
        }
        else{
            return Collections.emptyList();
        }
    }

    @Override
    public List<Movie> getMoviesByName(String name) {
         String base_url = "https://api.themoviedb.org/3/search/movie?";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(base_url)
                .queryParam("query", name)
                .queryParam("api_key",API_KEY);

        try {
            MovieResponse response = restTemplate.getForObject(uriBuilder.toUriString(), MovieResponse.class);
            return response.getResults();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
