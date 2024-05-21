package org.issk.dao;

import org.issk.dto.Movie;
import org.issk.dto.MovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class MovieAPIDaoAPIImpl implements MovieAPIDao {

    private final RestTemplate restTemplate;
    private static final String API_KEY = "89afb92d2b5bba942e667df05182f34a";


    @Autowired
    public MovieAPIDaoAPIImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Movie> getMoviesByGenre(String genre) {
       int genreId = getGenreIdFromGenreName(genre);
       String apiUrl="https://api.themoviedb.org/3/discover/movie?api_key=89afb92d2b5bba942e667df05182f34a";
        UriComponentsBuilder uriBuilder= UriComponentsBuilder.fromHttpUrl(apiUrl).queryParam("with_genres",genreId);

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
    public int getGenreIdFromGenreName(String genre) {
       //Code Here
       //select genreId from Genres where Genre = 'Drama'
        // return genreId
       return 878;
    }

    public String getUserPreferenceGenreIds(String userid) {
        //Code Here
        //select genreIds from Genres_Preference where userid = userid
        // return comma or pipe separated genreId
        //comma is AND and pipe is OR condition
        //Sample Movie Ape vs. Mecha Ape
        return "878|28";
    }

    @Override
    public List<Movie> getMoviesByRating(String rating, Boolean sort) {
        String sortOrder = "vote_average." + (sort ? "asc" : "desc");
        String apiUrl="https://api.themoviedb.org/3/discover/movie?api_key=89afb92d2b5bba942e667df05182f34a";
        UriComponentsBuilder uriBuilder= UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("sort_by",sortOrder)
                .queryParam("vote_average.gte",rating)
                .queryParam("vote_average.lte",rating);

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
    public List<Movie> getMoviesByPreferences(String userid) {
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
