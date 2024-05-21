import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.issk.dto.Genre;
import org.issk.mappers.GenreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovieAPIDaoAPIImpl {

    private static final String API_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=api_key=89afb92d2b5bba942e667df05182f34a";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GenreMapper genreMapper;

    @Transactional
    public void populateGenres() {
        List<Genre> genres = fetchGenresFromAPI();

        String sql = "INSERT INTO genres (id, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name)";

        for (Genre genre : genres) {
            jdbcTemplate.update(sql, genre.getId(), genre.getName());
        }
    }

    private List<Genre> fetchGenresFromAPI() {
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
                    genre.setId(genreNode.path("id").asInt());
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
