package org.issk.dao;

import org.issk.dto.Genre;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

public interface GenreDao {
    @Transactional
    boolean populateGenres();

    HashMap<Integer, Genre> getGenresFromDB();

    List<Genre> fetchGenresFromAPI();
}
