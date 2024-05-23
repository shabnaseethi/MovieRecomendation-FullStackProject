package org.issk.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.issk.dto.Genre;
import org.issk.mappers.GenreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class GenreDaoAPIImpl implements GenreDao{
    private static final String API_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=89afb92d2b5bba942e667df05182f34a";

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public GenreDaoAPIImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    @Transactional
    @Override
    public boolean populateGenres() {
        List<Genre> genres = fetchGenresFromAPI();

        String sql = "INSERT INTO genres (genreId, genreName) VALUES (?, ?);";

        int changed = 0;
        for (Genre genre : genres) {
            try {
                changed = jdbcTemplate.update(sql, genre.getGenreId(), genre.getName());
            } catch (DataAccessException e){
                System.out.println("Already in");
            }
        }
        return (changed>0);
    }

    @Override
    public HashMap<Integer, Genre> getGenresFromDB(){
        try {
            List<Genre> genresList = jdbcTemplate.query("SELECT * FROM genres;", new GenreMapper());
            HashMap<Integer, Genre> genresHash = new HashMap<Integer, Genre>();
            for (Genre genre : genresList){
                genresHash.put(genre.getGenreId(), genre);
            }
            return genresHash;
        } catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public List<Genre> fetchGenresFromAPI() {
        List<Genre> genres = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Parse JSON
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(result.toString());
                JsonNode genresNode = rootNode.path("genres");

                for (JsonNode genreNode : genresNode) {
                    Genre genre = new Genre();
                    genre.setGenreId(genreNode.path("id").asInt());
                    genre.setName(genreNode.path("name").asText());
                    genres.add(genre);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return genres;
    }
}
