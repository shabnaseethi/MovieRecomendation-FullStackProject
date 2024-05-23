package org.issk.mappers;

import org.issk.dto.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        Genre genre = new Genre();
        genre.setGenreId(resultSet.getInt("genreId"));
        genre.setName(resultSet.getString("genreName"));

        return genre;
    }
}
