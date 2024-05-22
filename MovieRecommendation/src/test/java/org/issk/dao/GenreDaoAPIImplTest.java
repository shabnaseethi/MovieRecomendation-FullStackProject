package org.issk.dao;

import org.issk.dto.Genre;
import org.issk.mappers.GenreMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJdbcTest
class GenreDaoAPIImplTest {
    private JdbcTemplate jdbcTemplate;
    private GenreDaoAPIImpl genreDao;

    @Autowired
    public GenreDaoAPIImplTest(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate =  jdbcTemplate;
        genreDao = new GenreDaoAPIImpl(jdbcTemplate);
    }
    @Test
    void populateGenres() {
        genreDao.populateGenres();
        Genre genre = (Genre) jdbcTemplate.queryForObject("SELECT * FROM genres LIMIT 1;", new GenreMapper());
        assertNotNull(genre, "Genre should not be null." );
        /* System.out.println(genre.getName());
        List<Genre> gList = jdbcTemplate.query("SELECT * FROM genres;", new GenreMapper());
        for(Genre g: gList){
            System.out.println(g.getName());
            }
         */

    }
}